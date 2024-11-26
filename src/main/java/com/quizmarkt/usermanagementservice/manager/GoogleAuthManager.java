package com.quizmarkt.usermanagementservice.manager;

/**
 * @author anercan
 */

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.quizmarkt.usermanagementservice.data.entity.AppConfig;
import com.quizmarkt.usermanagementservice.data.repository.AppConfigRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j
public class GoogleAuthManager {

    private static final String PACKAGE_NAME_LIFEINTHEUK = "com.quizmarkt.lifeintheuk";
    private final AppConfigRepository appConfigRepository;

    public Payload verifyToken(String idTokenString){
        try {
            String clientIdURL = getClientIdURL();
            if (clientIdURL == null) {
                log.error("verifyToken clientIdURL is null!");
            }
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(clientIdURL))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                // Get profile information from payload
               /* String email = payload.getEmail();
                boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
                String name = (String) payload.get("name");
                String pictureUrl = (String) payload.get("picture");
                String locale = (String) payload.get("locale");
                String familyName = (String) payload.get("family_name");
                String givenName = (String) payload.get("given_name");*/
                return idToken.getPayload();
            } else {
                return null;
            }
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getClientIdURL() {
        Optional<AppConfig> byPackageName = appConfigRepository.findByPackageName(PACKAGE_NAME_LIFEINTHEUK);
        return byPackageName.map(AppConfig::getGoogleAuthClientUrl).orElse(null);
    }
}
