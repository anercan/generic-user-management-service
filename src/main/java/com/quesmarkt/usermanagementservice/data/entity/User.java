package com.quesmarkt.usermanagementservice.data.entity;

import com.quesmarkt.usermanagementservice.data.enums.UserState;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * @author anercan
 */

@Data
@Document(collection = "users")
public class User {
    @Id
    private UUID id;
    private String email;
    private String username;
    private String password;
    private ZonedDateTime createdDate;
    private String avatarUrl;
    private Long appId;
    private UserState state;
    private String zone;
}
