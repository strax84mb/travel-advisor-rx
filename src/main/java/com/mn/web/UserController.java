package com.mn.web;

import com.mn.dtos.UserLoginRequest;
import com.mn.exceptions.IncorrectCredentialsException;
import com.mn.services.UserService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.rules.SecurityRule;
import lombok.extern.slf4j.Slf4j;

@Controller("/user")
@Slf4j
public class UserController {

    private UserService userService;
    private AppClient appClient;
    private TestClient testClient;

    public UserController(UserService userService, AppClient appClient, TestClient testClient) {
        this.userService = userService;
        this.appClient = appClient;
        this.testClient = testClient;
    }

    @Post("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Secured(SecurityRule.IS_ANONYMOUS)
    public String login(@Body UserLoginRequest request) {
        try {
            var creds = new UsernamePasswordCredentials(
                    request.getUsername(),
                    request.getPassword());
            var token = appClient.login(creds);
            return token.getAccessToken();
        } catch (Exception e) {
            throw new IncorrectCredentialsException();
        }
    }

    @Post("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_ANONYMOUS)
    public HttpResponse signup(@Body UserLoginRequest payload) {
        log.info("Success: ", userService.signup(payload));
        return HttpResponse.noContent();
    }

    @Get("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    @Secured(SecurityRule.IS_ANONYMOUS)
    public String hello() {
        return testClient.hello("Strale");
    }

}
