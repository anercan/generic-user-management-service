package com.quesmarkt.usermanagementservice.data.request;

import lombok.Data;

import java.util.Map;

/**
 * @author anercan
 */

@Data
public class SignInRequest {
    private String email;
    private String password;
    private Map<String,String> jwtClaims;
    private Long expirationDay;
}
