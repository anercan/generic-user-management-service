package com.quizmarkt.usermanagementservice.data.request;

import lombok.Data;

import java.util.Date;
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
    private Date expirationDate;
    private int appId;
}
