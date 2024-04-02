package com.fqrmix.authcenterback.services;

import com.fqrmix.authcenterback.services.impl.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

/**
 * Service class responsible for JWT token generation, validation, and extraction of user information.
 * <p>
 * How to generate keys:
 * <p>
 * ssh-keygen -t rsa -b 4096 -m PEM -f jwtRS256.key && \ <p>
 * openssl rsa -in jwtRS256.key -pubout -outform PEM -out jwtRS256.key.pub && \<p>
 * openssl pkcs8 -topk8 -inform PEM -in jwtRS256.key -out jwtRS256pkcs8.key -nocrypt && \<p>
 * cat jwtRS256pkcs8.key | base64 <p>
 */

@Service
@Slf4j
public class JWTService {
    @Value("${authcenterback.jwtSecret}")
    private String jwtPrivateKey;

    @Value("${authcenterback.jwtPublic}")
    private String jwtPublicKey;

    @Value("${authcenterback.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * Generates a JWT token based on the provided authentication details.
     *
     * @param authentication An object representing the authentication details of the user.
     * @return The generated JWT token.
     */
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getPrivateKey())
                .compact();
    }

    /**
     * Retrieves the private key used for JWT token generation.
     *
     * @return The private key.
     */
    private PrivateKey getPrivateKey() {
        KeyFactory keyFactory;

        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        byte[] keyBytes = Base64.decodeBase64(
                new String(Base64.decodeBase64(jwtPrivateKey))
                        .replace("-----BEGIN PRIVATE KEY-----", "")
                        .replace("-----END PRIVATE KEY-----", "")
                        .replaceAll("\\n", ""));
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(keyBytes);

        try {
            return keyFactory.generatePrivate(privateKeySpec);
        } catch (InvalidKeySpecException e) {
            log.error("Unable to call getPrivateKey(): {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the public key used for JWT token validation.
     *
     * @return The public key.
     */
    private PublicKey getPublicKey() {

        KeyFactory keyFactory;

        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        byte[] keyBytes = Base64.decodeBase64(
                new String(Base64.decodeBase64(jwtPublicKey))
                        .replace("-----BEGIN PUBLIC KEY-----", "")
                        .replace("-----END PUBLIC KEY-----", "")
                        .replaceAll("\\n", ""));
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(keyBytes);

        try {
            return keyFactory.generatePublic(publicKeySpec);
        } catch (InvalidKeySpecException e) {
            log.error("Unable to call getPublicKey(): {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Validates the provided JWT token.
     *
     * @param authToken The JWT token to be validated.
     * @return True if the token is valid; false otherwise.
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(getPublicKey())
                    .build()
                    .parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    /**
     * Extracts the username from the provided JWT token.
     *
     * @param authToken The JWT token from which to extract the username.
     * @return The username extracted from the JWT token.
     */
    public String getUserNameFromJwtToken(String authToken) {
        return Jwts.parser()
                .verifyWith(getPublicKey())
                .build()
                .parseSignedClaims(authToken)
                .getPayload()
                .getSubject();
    }

    /**
     * Extracts the expiration date from the provided JWT token.
     *
     * @param authToken The JWT token from which to extract the username.
     * @return The expiration date extracted from the JWT token.
     */
    public Date getExpirationDateFromJwtToken(String authToken) {
        return Jwts.parser()
                .verifyWith(getPublicKey())
                .build()
                .parseSignedClaims(authToken)
                .getPayload()
                .getExpiration();
    }
}
