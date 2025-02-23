package org.klb.userservice.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ApiResponseWrapper<T> {
    @Schema(description = "HTTP status code of the response", example = "201")
    private int status;

    @Schema(description = "Message accompanying the status code", example = "User registered successfully")
    private String message;

    @Schema(description = "Data returned in the response")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public ApiResponseWrapper(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ApiResponseWrapper(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
