package com.quesmarkt.usermanagementservice.controller;

import com.quesmarkt.usermanagementservice.data.response.ServiceResponse;
import org.springframework.http.ResponseEntity;

/**
 * @author anercan
 */
public class BaseController {

    protected <T> ResponseEntity<T> createResponseEntity(ServiceResponse<T> serviceResponse) {
        return new ResponseEntity<>(serviceResponse.getValue(), serviceResponse.getStatus());
    }

}
