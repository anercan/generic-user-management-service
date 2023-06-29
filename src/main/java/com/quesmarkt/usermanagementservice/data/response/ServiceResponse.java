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


}
