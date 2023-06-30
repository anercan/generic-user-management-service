package com.quesmarkt.usermanagementservice.data.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * @author anercan
 */

@Data
public class ServiceResponse<T> {

    private HttpStatus status = HttpStatus.OK;
    private T value;
    private String message = "";

    public ServiceResponse(T value) {
        this.value = value;
    }

    public ServiceResponse(HttpStatus status, T value, String message) {
        this.status = status;
        this.value = value;
        this.message = message;
    }
}