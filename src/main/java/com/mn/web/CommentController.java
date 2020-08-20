package com.mn.web;

import com.mn.dtos.ChangeCommentRequest;
import com.mn.dtos.CommentResponse;
import com.mn.dtos.WriteCommentRequest;
import com.mn.services.CommentService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import lombok.extern.slf4j.Slf4j;

import java.security.Principal;

@Controller("/comment")
@Slf4j
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Post
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public CommentResponse postComment(@Body WriteCommentRequest request, Principal principal) {
        return commentService.save(principal.getName(), request.getCityId(), request.getMessage())
                .blockingGet();
    }

    @Put("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public CommentResponse changeComment(@PathVariable("id") Long id, @Body ChangeCommentRequest request, Principal principal) {
        return commentService.update(id, principal.getName(), request.getMessage());
    }

    @Delete("/{id}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse deleteComment(@PathVariable("id") Long id, Principal principal) {
        var result = commentService.delete(id, principal.getName());
        return result ? HttpResponse.noContent() : HttpResponse.serverError();
    }
}
