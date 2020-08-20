package com.mn.services;

import com.mn.dtos.CommentResponse;
import io.reactivex.Single;

public interface CommentService {

    Single<CommentResponse> save(String username, Long cityId, String text);

    CommentResponse update(Long id, String username, String text);

    boolean delete(Long id, String username);
}
