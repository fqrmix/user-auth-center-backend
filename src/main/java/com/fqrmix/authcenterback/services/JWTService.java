package com.fqrmix.authcenterback.services;

import com.fqrmix.authcenterback.services.impl.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

import static io.jsonwebtoken.Jwts.parser;


/**
 * Service class responsible for JWT token generation, validation, and extraction of user information.
 */
@Service
@Slf4j
public class JWTService {
    @Value("${authcenterback.jwtSecret}")
    private String jwtSecret;

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
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Retrieves the signing key used for JWT token generation and validation.
     *
     * @return The signing key.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
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
                    .verifyWith(getSigningKey())
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
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(authToken)
                .getPayload()
                .getSubject();
    }
}
