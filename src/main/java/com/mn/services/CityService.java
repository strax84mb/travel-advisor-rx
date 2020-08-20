package com.mn.services;

import com.mn.dtos.CityResponse;
import com.mn.dtos.WriteCityRequest;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface CityService {

    Maybe<CityResponse> getById(Long id, Integer maxComments);

    Flowable<CityResponse> getAll(Integer maxComments);

    Single<CityResponse> save(WriteCityRequest dto);

    Single<CityResponse> update(Long id, WriteCityRequest dto);

    boolean delete(Long id);
}
