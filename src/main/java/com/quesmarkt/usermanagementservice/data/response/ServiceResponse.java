package com.quesmarkt.usermanagementservice.data.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * @author anercan
 */

@Getter
@Setter
@SuperBuilder
@ToString
@NoArgsConstructor
public class ServiceResponse {
    private Integer status;
    private String message;
}
