package com.mn.web;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.client.annotation.Client;

@Client("olddemo")
public interface TestClient {

    @Get("/hello/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    String hello(@PathVariable("name") String name);
}
