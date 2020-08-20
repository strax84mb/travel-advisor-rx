package com.mn.exceptions.handlers;

import com.mn.exceptions.UsernameNotFoundException;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;

import javax.inject.Singleton;

@Produces
@Singleton
@Requires(classes = {UsernameNotFoundException.class})
public class UsernameNotFoundExceptionHandler implements ExceptionHandler<UsernameNotFoundException, HttpResponse> {

    @Override
    public HttpResponse handle(HttpRequest request, UsernameNotFoundException exception) {
        return HttpResponse.notFound(exception.getMessage());
    }
}
