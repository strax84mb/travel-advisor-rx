package com.mn.repositories;

import com.mn.entities.Comment;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    long update(@Id Long id, String text, LocalDateTime modified);
}
