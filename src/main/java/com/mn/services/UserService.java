package com.mn.services;

import com.mn.dtos.UserLoginRequest;

public interface UserService {

    Boolean signup(UserLoginRequest request);
}
