package com.quizmarkt.usermanagementservice.data.request;

import com.quizmarkt.usermanagementservice.data.enums.PremiumType;
import lombok.Data;

/**
 * @author anercan
 */

@Data
public class UserFilterRequest {

    private PremiumType premiumType;
    private String id;

}
