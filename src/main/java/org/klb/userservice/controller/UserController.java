package org.klb.userservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.klb.userservice.controller.dto.ApiResponseWrapper;
import org.klb.userservice.controller.dto.request.UserReq;
import org.klb.userservice.controller.dto.response.UserRes;
import org.klb.userservice.service.UserService;
import org.klb.userservice.util.MessageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final MessageUtil messageUtil;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<UserRes>> getUserById(@PathVariable Long id) {
        UserRes userRes = userService.getUserById(id);
        return ResponseEntity.ok(
                new ApiResponseWrapper<>(
                        HttpStatus.OK.value(),
                        messageUtil.getMessage("success.user.found", id),
                        userRes
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponseWrapper<List<UserRes>>> getAllUsers() {
        List<UserRes> users = userService.getAllUsers();
        return ResponseEntity.ok(
                new ApiResponseWrapper<>(
                        HttpStatus.OK.value(),
                        messageUtil.getMessage("success.user.list"),
                        users
                )
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponseWrapper<UserRes>> createUser(@Valid @RequestBody UserReq userReq) {
        UserRes userRes = userService.createUser(userReq);
        return new ResponseEntity<>(
                new ApiResponseWrapper<>(
                        HttpStatus.CREATED.value(),
                        messageUtil.getMessage("success.user.created"),
                        userRes
                ),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<UserRes>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserReq userReq
    ) {
        UserRes updatedUser = userService.updateUser(id, userReq);
        return ResponseEntity.ok(
                new ApiResponseWrapper<>(
                        HttpStatus.OK.value(),
                        messageUtil.getMessage("success.user.updated", id),
                        updatedUser
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(
                new ApiResponseWrapper<>(
                        HttpStatus.OK.value(),
                        messageUtil.getMessage("success.user.deleted", id)
                )
        );
    }
}
