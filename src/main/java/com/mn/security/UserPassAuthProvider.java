package com.mn.security;

import com.mn.exceptions.IncorrectCredentialsException;
import com.mn.exceptions.UsernameNotFoundException;
import com.mn.repositories.UserRepository;
import com.mn.services.EncodingService;
import com.mn.services.UserService;
import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.UserDetails;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import org.reactivestreams.Publisher;

import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.util.List;

@Singleton
@Transactional
public class UserPassAuthProvider implements AuthenticationProvider {

    private EncodingService encodingService;
    private UserRepository userRepository;

    public UserPassAuthProvider(EncodingService encodingService, UserRepository userRepository) {
        this.encodingService = encodingService;
        this.userRepository = userRepository;
    }

    @Override
    public Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest,
                                                          AuthenticationRequest<?, ?> authenticationRequest) {
        return Flowable.create(emitter -> {
            var maybeUser = Single.just(authenticationRequest.getIdentity().toString())
                    .map(userRepository::findByUsername).blockingGet();
            if (maybeUser.isEmpty().blockingGet()) {
                emitter.onError(new UsernameNotFoundException(authenticationRequest.getIdentity().toString()));
            } else {
                var user = maybeUser.blockingGet();
                var pass = encodingService.encode(authenticationRequest.getSecret().toString(), user.getSalt());
                if (user.getPassword().equals(pass)) {
                    var roles = List.of(user.getRole().name());
                    emitter.onNext(new UserDetails(authenticationRequest.getIdentity().toString(), roles));
                    emitter.onComplete();
                } else {
                    emitter.onError(new IncorrectCredentialsException());
                }
            }
        }, BackpressureStrategy.ERROR);
    }
}
