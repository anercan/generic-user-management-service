package com.quesmarkt.usermanagementservice.service;

import com.quesmarkt.usermanagementservice.data.response.ServiceResponse;
import org.springframework.http.HttpStatus;

import java.util.logging.Logger;

/**
 * @author anercan
 */
public class BaseService {

    Logger log = Logger.getAnonymousLogger();

    <T> ServiceResponse<T> createFailResult(String message) {
        return new ServiceResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, null, message);
    }

    protected <T>boolean isSucceed(ServiceResponse<T> serviceResponse){
        if (serviceResponse != null && serviceResponse.getStatus() != null ) {
            return HttpStatus.Series.SUCCESSFUL == HttpStatus.Series.valueOf(serviceResponse.getStatus().value());
        } else {
            return false;
        }
    }
}
