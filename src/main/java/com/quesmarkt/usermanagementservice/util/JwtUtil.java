package com.quesmarkt.usermanagementservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtil {

    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    public static final String USERNAME = "username";
    public static final String USER_ID = "userId";
    public static final String REFRESH = "refresh";

    private static String getSecretKey() {
        return System.getenv("JWT_SECRET");
    }

    public static String createJWT(String id, String username) {
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(username)) {
            return null;
        }
        try {
            return Jwts.builder().setClaims(getClaims(id, username))
                    .setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + getSessionTime()))
                    .setSubject(REFRESH).signWith(getKey()).compact();
        } catch (Exception e) {
            return null;
        }
    }

    private static long getSessionTime() {
        return 1800000L;
    }

    private static Claims getClaims(String id, String username) {
        Claims claims = Jwts.claims();
        claims.put(USER_ID, id);
        claims.put(USERNAME, username);
        return claims;
    }

    public static boolean checkJWT(String jwt) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        try {
            Jwts.parserBuilder().setSigningKey(getKey()).build().parse(jwt);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static SecretKeySpec getKey() {
        return new SecretKeySpec(getSecretKey().getBytes(StandardCharsets.UTF_8), SIGNATURE_ALGORITHM.getJcaName());
    }

    public static String extractUsername(String jwt) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        try {
            return (String) Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build().parseClaimsJws(jwt).getBody().get(USERNAME);
        } catch (Exception e) {
            return "";
        }
    }

}