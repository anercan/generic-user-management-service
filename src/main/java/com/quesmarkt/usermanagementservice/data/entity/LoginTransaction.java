package com.quesmarkt.usermanagementservice.data.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * @author anercan
 */

@Data
@Document(collection = "loginTransaction")
public class LoginTransaction {

    private String userId;
    private LocalDateTime date;
    private String ip;
    private String zone;
    private boolean isLoginSucceed;

}
