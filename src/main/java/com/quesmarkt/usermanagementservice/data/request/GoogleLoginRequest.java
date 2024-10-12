package com.quesmarkt.usermanagementservice.data.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author anercan
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class GoogleLoginRequest extends SignInRequest {
    private String token;
}
