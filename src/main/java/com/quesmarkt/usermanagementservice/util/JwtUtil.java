package com.quesmarkt.usermanagementservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    private static final String USERNAME = "username";
    private static final String USER_ID = "user-id";
    private static final String REFRESH = "refresh";
    private static final String JWT_SECRET = System.getProperty("JWT_SECRET");

    public static String createJWT(String id, String username, Map<String, String> jwtClaims, Long days) {
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(username)) {
            return null;
        }
        try {
            return Jwts.builder()
                    .setClaims(getClaims(id, username, jwtClaims))
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + getSessionTime(days)))
                    .setSubject(REFRESH)
                    .signWith(getKey())
                    .compact();
        } catch (Exception e) {
            return null;
        }
    }

    private static long getSessionTime(Long days) {
        if (days != null && days != 0) {
            return days * 24 * 60 * 60 * 1000L;
        }
        return 24 * 60 * 60 * 1000L;
    }

    private static Claims getClaims(String id, String username, Map<String, String> jwtClaims) {
        Claims claims = Jwts.claims();
        claims.put(USER_ID, id);
        claims.put(USERNAME, username);
        if (!CollectionUtils.isEmpty(jwtClaims)) {
            claims.putAll(jwtClaims);
        }
        return claims;
    }

    private static SecretKeySpec getKey() {
        return new SecretKeySpec(getJwtSecret().getBytes(StandardCharsets.UTF_8), SIGNATURE_ALGORITHM.getJcaName());
    }

    public static String getJwtSecret() {
        return StringUtils.isEmpty(JWT_SECRET) ? "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJJc3N1ZXIiOiJJc3N" : JWT_SECRET;
    }
}