package com.mn.services.impl;

import com.mn.dtos.CityResponse;
import com.mn.dtos.CommentResponse;
import com.mn.dtos.WriteCityRequest;
import com.mn.entities.City;
import com.mn.entities.Comment;
import com.mn.exceptions.CityNotFoundException;
import com.mn.repositories.CityRepository;
import com.mn.repositories.CommentRxRepository;
import com.mn.services.CityService;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.Sort;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.util.List;

@Singleton
@Transactional
@Slf4j
public class CityServiceImpl implements CityService {

    private CityRepository cityRepository;
    private CommentRxRepository commentRxRepository;

    public CityServiceImpl(CityRepository cityRepository, CommentRxRepository commentRxRepository) {
        this.cityRepository = cityRepository;
        this.commentRxRepository = commentRxRepository;
    }

    @Override
    public Maybe<CityResponse> getById(Long id, Integer maxComments) {
        Flowable<Comment> comments;
        if (maxComments == 0) {
            comments = Flowable.empty();
        } else {
            var pageable = Pageable.from(0, maxComments, Sort.of(Sort.Order.desc("created")));
            comments = commentRxRepository.findByCityId(id, pageable);
        }
        var commentList = comments.map(c -> CommentResponse.builder()
                .created(c.getCreated())
                .modified(c.getModified())
                .poster(c.getPoster().getUsername())
                .id(c.getId())
                .text(c.getText())
                .build()).toList().toMaybe();
        return cityRepository.findById(id)
                .zipWith(commentList, (city, list) -> CityResponse.builder()
                        .id(city.getId())
                        .name(city.getName())
                        .country(city.getCountry())
                        .comments(list)
                        .build());
    }

    @Override
    public Flowable<CityResponse> getAll(Integer maxComments) {
        return cityRepository.findAll()
                .map(city -> toDto(city, maxComments));
    }

    @Override
    public Single<CityResponse> save(WriteCityRequest request) {
        return Single.just(request)
                .map(dto -> City.builder().name(dto.getName()).country(dto.getCountry()).build())
                .map(cityRepository::save)
                .blockingGet()
                .map(city -> toDto(city, 0));
    }

    @Override
    public Single<CityResponse> update(Long id, WriteCityRequest dto) {
        return cityRepository.findById(id).map(city -> {
            city.setName(dto.getName());
            city.setCountry(dto.getCountry());
            return city;
        })
                .map(cityRepository::save)
                .blockingGet()
                .map(city -> toDto(city, 0));
    }

    @Override
    public boolean delete(Long id) {
        if (!cityRepository.existsById(id).blockingGet()) {
            throw new CityNotFoundException(id);
        }
        // TODO commentRepository.deleteByCityId(id);
        var ex = cityRepository.deleteById(id).blockingGet();
        if (ex != null) {
            log.error("Error while deleting city", ex);
        }
        return ex == null;
    }

    private CityResponse toDto(City city, int maxComments) {
        List<CommentResponse> comments = null;
        if (maxComments > 0) {
            comments = commentRxRepository.findByCityId(city.getId(),
                    Pageable.from(0, maxComments, Sort.of(Sort.Order.desc("created"))))
                    .publish(c -> c)
                    .map(c -> CommentResponse.builder()
                            .created(c.getCreated())
                            .modified(c.getModified())
                            .poster(c.getPoster().getUsername())
                            .id(c.getId())
                            .text(c.getText())
                            .build())
                    .toList().blockingGet();
        }
        return CityResponse.builder()
                .id(city.getId())
                .name(city.getName())
                .country(city.getCountry())
                .comments(comments)
                .build();
    }
}
