package com.quesmarkt.usermanagementservice.data.response;

import lombok.Builder;
import lombok.Data;

/**
 * @author anercan
 */

@Data
@Builder
public class SignUpResponse {

    String jwt;

}
