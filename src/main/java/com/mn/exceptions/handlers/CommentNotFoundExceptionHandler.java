package com.mn.exceptions.handlers;

import com.mn.exceptions.CommentNotFoundException;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;

import javax.inject.Singleton;

@Produces
@Singleton
@Requires(classes = {CommentNotFoundException.class})
public class CommentNotFoundExceptionHandler implements ExceptionHandler<CommentNotFoundException, HttpResponse> {

    @Override
    public HttpResponse handle(HttpRequest request, CommentNotFoundException exception) {
        return HttpResponse.notFound(exception.getMessage());
    }
}
