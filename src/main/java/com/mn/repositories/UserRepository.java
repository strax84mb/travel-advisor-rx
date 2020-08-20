package com.mn.repositories;

import com.mn.entities.User;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.reactivex.Maybe;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Maybe<User> findByUsername(String username);
}
