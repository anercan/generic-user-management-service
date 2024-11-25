package com.quizmarkt.usermanagementservice.util;

import com.quizmarkt.usermanagementservice.data.enums.PremiumType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Slf4j
public class JwtUtil {
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    private static final String USERNAME = "username";
    private static final String APP_ID = "app-id";
    private static final String USER_ID = "user-id";
    private static final String REFRESH = "refresh";
    public static final String PREMIUM_TYPE = "premium-type";
    private static final String JWT_SECRET = System.getenv("JWT_SECRET");

    public static String createJWT(String id, Map<String, String> jwtClaims, Date expirationDate, int appId, PremiumType premiumType) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        try {
            return Jwts.builder()
                    .setClaims(getClaims(id, jwtClaims, appId, premiumType))
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(expirationDate)
                    .setSubject(REFRESH)
                    .signWith(getKey())
                    .compact();
        } catch (Exception e) {
            log.error("createJWT got exception userID:{}", id, e);
            return null;
        }
    }

    private static Claims getClaims(String id, Map<String, String> jwtClaims, int appId, PremiumType premiumType) {
        Claims claims = Jwts.claims();
        claims.put(USER_ID, id);
        claims.put(APP_ID, appId);
        claims.put(PREMIUM_TYPE, premiumType);
        if (!CollectionUtils.isEmpty(jwtClaims)) {
            claims.putAll(jwtClaims);
        }
        return claims;
    }

    private static SecretKeySpec getKey() {
        return new SecretKeySpec(getJwtSecret().getBytes(StandardCharsets.UTF_8), SIGNATURE_ALGORITHM.getJcaName());
    }

    public static String getJwtSecret() {
        return StringUtils.isEmpty(JWT_SECRET) ? "local_key" : JWT_SECRET;
    }

}