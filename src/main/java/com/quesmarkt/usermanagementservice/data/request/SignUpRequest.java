package com.quesmarkt.usermanagementservice.data.request;

import lombok.Data;

/**
 * @author anercan
 */

@Data
public class SignUpRequest {
    private String mail;
    private String password;
    private String username;
}
