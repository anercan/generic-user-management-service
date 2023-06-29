package com.quesmarkt.usermanagementservice.data.request;

import lombok.Data;

/**
 * @author anercan
 */

@Data
public class SignInRequest {

    private String email;
    private String password;

}
