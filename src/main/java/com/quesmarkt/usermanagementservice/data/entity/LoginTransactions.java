package com.quesmarkt.usermanagementservice.data.entity;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * @author anercan
 */
public class LoginTransactions {

    private UUID userId;
    private ZonedDateTime date;
    private String ip;
    private String zone;

}
