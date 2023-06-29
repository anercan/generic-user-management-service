package com.quesmarkt.usermanagementservice.data.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;

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
    private ZonedDateTime createdDate;
    private ZonedDateTime lastLogin;
    private String avatarUrl;
    private boolean isActive;
}
