package com.quesmarkt.usermanagementservice.data.entity;

import com.quesmarkt.usermanagementservice.data.enums.UserState;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * @author anercan
 */

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String email;
    private String username;
    private String password;
    private LocalDateTime createdDate;
    private String avatarUrl;
    private Long appId;
    private UserState state;
    private String zone;
}
