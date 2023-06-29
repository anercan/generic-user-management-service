package com.quesmarkt.usermanagementservice.data.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author anercan
 */

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String name;
}
