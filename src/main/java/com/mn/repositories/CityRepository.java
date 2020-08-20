package com.mn.repositories;

import com.mn.entities.City;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.reactive.RxJavaCrudRepository;

@Repository
public interface CityRepository extends RxJavaCrudRepository<City, Long> {
}
