package com.quesmarkt.usermanagementservice.manager.exception;

/**
 * @author anercan
 */

public class AppException extends RuntimeException {

    public AppException(Exception e) {
        super(e.getMessage(), e.getCause());
    }

    public AppException(String message) {
        super(message);
    }

}
