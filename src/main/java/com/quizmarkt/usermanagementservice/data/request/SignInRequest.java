package com.quizmarkt.usermanagementservice.data.request;

import com.quizmarkt.usermanagementservice.data.enums.OSType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

/**
 * @author anercan
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {
    private String email;
    private String password;
    private Map<String, String> jwtClaims;
    private Date expirationDate;
    private Integer appId;
    private SignInType signInType;
    private DeviceInfo deviceInfo;

    @Data
    public static class DeviceInfo {
        String token;
        OSType osType;

        @Override
        public String toString() {
            return "DeviceInfo{" +
                    "token='" + token + '\'' +
                    ", osType=" + osType +
                    '}';
        }
    }
}
