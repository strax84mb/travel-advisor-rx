package com.mn.repositories;

import com.mn.entities.Comment;
import io.micronaut.data.annotation.Join;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.reactive.RxJavaCrudRepository;
import io.reactivex.Flowable;

@Repository
public interface CommentRxRepository extends RxJavaCrudRepository<Comment, Long> {

    @Join(value = "poster", type = Join.Type.FETCH)
    Flowable<Comment> findByCityId(Long cityId, Pageable pageable);

    //Single<Integer> deleteByCityId(Long id);
}
