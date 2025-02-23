package org.klb.userservice.controller.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserReq {
    @NotBlank(message = "{user.name.notBlank}")
    @Size(min = 3, max = 50, message = "{user.name.size}")
    private String name;

    @NotBlank(message = "{user.email.notBlank}")
    @Email(message = "{user.email.invalid}")
    private String email;

    @NotBlank(message = "{user.password.notBlank}")
    @Size(min = 6, message = "{user.password.size}")
    private String password;

    @NotBlank(message = "{user.status.notBlank}")
    private String status;
}
