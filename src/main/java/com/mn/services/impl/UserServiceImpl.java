package com.mn.services.impl;

import com.mn.dtos.UserLoginRequest;
import com.mn.entities.User;
import com.mn.entities.UserRole;
import com.mn.exceptions.UsernameTakenException;
import com.mn.repositories.UserRepository;
import com.mn.services.EncodingService;
import com.mn.services.UserService;
import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;

@Singleton
@Slf4j
public class UserServiceImpl implements UserService {

    private EncodingService encodingService;
    private UserRepository userRepository;

    public UserServiceImpl(EncodingService encodingService, UserRepository userRepository) {
        this.encodingService = encodingService;
        this.userRepository = userRepository;
    }

    @Override
    public Boolean signup(UserLoginRequest request) {
        var usernameCheck = Single.just(request.getUsername())
                .map(userRepository::findByUsername);
        var createEntity = Single.just(request)
                .map(req -> {
                    var bytes = encodingService.generateSalt();
                    return User.builder()
                            .role(UserRole.USER)
                            .salt(bytes)
                            .username(req.getUsername())
                            .password(encodingService.encode(req.getPassword(), bytes))
                            .build();
                });
        return createEntity.zipWith(usernameCheck, (entity, loaded) -> {
            if (loaded.isEmpty().blockingGet()) {
                return entity;
            } else {
                throw new UsernameTakenException(entity.getUsername());
            }
        }).map(u -> {
            try {
                userRepository.save(u);
                return true;
            } catch (Exception e) {
                log.error("Error whie saving user", e);
                return false;
            }
        }).blockingGet();
    }
}
