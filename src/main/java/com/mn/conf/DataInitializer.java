package com.mn.conf;

import com.mn.entities.City;
import com.mn.entities.Comment;
import com.mn.entities.User;
import com.mn.entities.UserRole;
import com.mn.repositories.CityRepository;
import com.mn.repositories.CommentRxRepository;
import com.mn.repositories.UserRepository;
import com.mn.services.EncodingService;
import io.micronaut.discovery.event.ServiceReadyEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.scheduling.annotation.Async;

import javax.inject.Singleton;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Singleton
public class DataInitializer {

    private UserRepository userRepository;
    private CityRepository cityRepository;
    private CommentRxRepository commentRxRepository;
    private EncodingService encodingService;

    public DataInitializer(UserRepository userRepository, CityRepository cityRepository, CommentRxRepository commentRxRepository, EncodingService encodingService) {
        this.userRepository = userRepository;
        this.cityRepository = cityRepository;
        this.commentRxRepository = commentRxRepository;
        this.encodingService = encodingService;
    }

    @EventListener
    @Async
    public void initAdmins(final ServiceReadyEvent event) throws NoSuchAlgorithmException {
        var salt = encodingService.generateSalt();
        var admin = User.builder()
                .username("admin")
                .password(encodingService.encode("admin", salt))
                .role(UserRole.ADMIN)
                .salt(salt)
                .build();
        admin = userRepository.save(admin);

        var city = City.builder()
                .name("Novi Sad")
                .country("Srbija")
                .build();
        city = cityRepository.save(city).blockingGet();
        var com = Comment.builder()
                .city(city)
                .created(LocalDateTime.now())
                .modified(LocalDateTime.now())
                .poster(admin)
                .text("Post one")
                .build();
        commentRxRepository.save(com).blockingGet();
        com = Comment.builder()
                .city(city)
                .created(LocalDateTime.now())
                .modified(LocalDateTime.now())
                .poster(admin)
                .text("Post two")
                .build();
        commentRxRepository.save(com).blockingGet();
        com = Comment.builder()
                .city(city)
                .created(LocalDateTime.now())
                .modified(LocalDateTime.now())
                .poster(admin)
                .text("Post three")
                .build();
        commentRxRepository.save(com).blockingGet();
    }
}
