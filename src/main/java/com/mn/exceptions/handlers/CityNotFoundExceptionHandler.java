package com.mn.exceptions.handlers;

import com.mn.exceptions.CityNotFoundException;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;

import javax.inject.Singleton;

@Produces
@Singleton
@Requires(classes = {CityNotFoundException.class})
public class CityNotFoundExceptionHandler implements ExceptionHandler<CityNotFoundException, HttpResponse> {

    @Override
    public HttpResponse handle(HttpRequest request, CityNotFoundException exception) {
        return HttpResponse.notFound(exception.getMessage());
    }
}
