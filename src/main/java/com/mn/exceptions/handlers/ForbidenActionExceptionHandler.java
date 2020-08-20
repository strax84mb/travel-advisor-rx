package com.mn.exceptions.handlers;

import com.mn.exceptions.ForbidenActionException;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;

import javax.inject.Singleton;

@Produces
@Singleton
@Requires(classes = {ForbidenActionException.class})
public class ForbidenActionExceptionHandler implements ExceptionHandler<ForbidenActionException, HttpResponse> {

    @Override
    public HttpResponse handle(HttpRequest request, ForbidenActionException exception) {
        return HttpResponse.status(HttpStatus.FORBIDDEN, exception.getMessage());
    }
}
