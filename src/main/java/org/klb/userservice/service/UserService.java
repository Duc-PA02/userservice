package org.klb.userservice.service;

import org.klb.userservice.controller.dto.request.UserReq;
import org.klb.userservice.controller.dto.response.UserRes;

import java.util.List;

public interface UserService {
    UserRes getUserById(Long userId);
    List<UserRes> getAllUsers();
    UserRes createUser(UserReq userReq);
    UserRes updateUser(Long userId, UserReq userReq);
    void deleteUser(Long userId);
}
