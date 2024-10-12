package com.quesmarkt.usermanagementservice.data.request;

import lombok.Data;

import java.util.Map;

/**
 * @author anercan
 */

@Data
public class SignUpRequest {
    private String mail;
    private String password;
    private String username;
    private Map<String,String> jwtClaims;
    private Long expirationDay;
    private int appId;
}
