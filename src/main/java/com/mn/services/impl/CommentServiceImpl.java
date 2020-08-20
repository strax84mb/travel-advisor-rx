package com.mn.services.impl;

import com.mn.dtos.CommentResponse;
import com.mn.entities.Comment;
import com.mn.entities.UserRole;
import com.mn.exceptions.CityNotFoundException;
import com.mn.exceptions.CommentNotFoundException;
import com.mn.exceptions.ForbidenActionException;
import com.mn.exceptions.UsernameNotFoundException;
import com.mn.repositories.CityRepository;
import com.mn.repositories.CommentRepository;
import com.mn.repositories.CommentRxRepository;
import com.mn.repositories.UserRepository;
import com.mn.services.CommentService;
import io.reactivex.Maybe;
import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

@Singleton
@Transactional
@Slf4j
public class CommentServiceImpl implements CommentService {

    private CommentRxRepository commentRxRepository;
    private CommentRepository commentRepository;
    private UserRepository userRepository;
    private CityRepository cityRepository;

    public CommentServiceImpl(CommentRxRepository commentRxRepository, CommentRepository commentRepository,
                              UserRepository userRepository, CityRepository cityRepository) {
        this.commentRxRepository = commentRxRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.cityRepository = cityRepository;
    }

    @Override
    public Single<CommentResponse> save(String username, Long cityId, String text) {
        var maybeCity = cityRepository.findById(cityId)
                .doOnComplete(() -> {throw new CityNotFoundException(cityId);});
        var maybeUser = userRepository.findByUsername(username)
                .doOnComplete(() -> {throw new UsernameNotFoundException(username);});
        return Maybe.zip(Maybe.just(text), maybeCity, maybeUser, (txt, city, user) -> Comment.builder()
                .text(txt)
                .city(city)
                .poster(user)
                .created(LocalDateTime.now())
                .modified(LocalDateTime.now())
                .build()
        )
                .map(commentRxRepository::save)
                .map(c -> c.blockingGet())
                .toSingle()
                .map(c -> toDto(c));
    }

    @Override
    public CommentResponse update(Long id, String username, String text) {
        var maybeUser = userRepository.findByUsername(username)
                .doOnComplete(() -> {throw new UsernameNotFoundException(username);});
        var haveAGo = commentRxRepository.findById(id)
                .doOnComplete(() -> {throw new CommentNotFoundException(id);})
                .zipWith(maybeUser, (comment, user) -> {
                    if (!comment.getPoster().getId().equals(user.getId())) {
                        throw new ForbidenActionException("Only original poster can change the comment");
                    }
                    return true;
                }).blockingGet();
        if (haveAGo) {
            var comment = commentRepository.findById(id);
            var result = comment.map(c -> {
                c.setText(text);
                c.setModified(LocalDateTime.now());
                commentRepository.save(c);
                return c;
            }).get();
            return toDto(result);
        }
        return null;
    }

    @Override
    public boolean delete(Long id, String username) {
        var maybeUser = userRepository.findByUsername(username)
                .doOnComplete(() -> {throw new UsernameNotFoundException(username);});
        var result = new AtomicBoolean(true);
        commentRxRepository.findById(id)
                .doOnComplete(() -> {throw new CommentNotFoundException(id);})
                .zipWith(maybeUser, (comment, user) -> {
                    if (!UserRole.ADMIN.equals(user.getRole()) && !comment.getPoster().getId().equals(user.getId())) {
                        throw new ForbidenActionException("Only admin or original poster can delete comment");
                    }
                    return comment;
                })
                .map(c -> commentRxRepository.deleteById(c.getId()))
                .doOnError(e -> {
                    e.printStackTrace();
                    log.error("Error while deleting comment", e);
                    result.set(false);
                })
                .blockingGet();
        return result.get();
    }

    private CommentResponse toDto(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .text(comment.getText())
                .poster(comment.getPoster().getUsername())
                .created(comment.getCreated())
                .modified(comment.getModified())
                .build();
    }
}
