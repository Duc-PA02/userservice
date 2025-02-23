package org.klb.userservice.controller.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRes {
    private String id;
    private String name;
    private String email;
    private String status;
}
