package com.quizmarkt.usermanagementservice.data.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * @author anercan
 */

@Getter
@Setter
@SuperBuilder
public class SignInResponse extends ServiceResponse {
    private String jwt;
}
