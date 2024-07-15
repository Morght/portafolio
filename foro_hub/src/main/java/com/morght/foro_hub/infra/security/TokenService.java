package com.morght.foro_hub.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.morght.foro_hub.models.User;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class TokenService {

    @Value("${spring.application.name}")
    private String issuer;
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private byte jwtExpirationTime;
    // TODO Refresh token exp
    // @Value("${application.security.jwt.refresh-token.expiration}")
    // private long jwtRefreshExpirationTime;

    public String createToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(user.getEmail())
                    .withClaim("id", user.getId())
                    .withExpiresAt(getExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException();
        }
    }

    public String getSubject(String token) {
        if (token == null)
            throw new RuntimeException();

        DecodedJWT verifier = null;

        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(token);
            verifier.getSubject();
        } catch (JWTVerificationException exception) {
            System.out.println(exception.toString());
        }

        if (verifier.getSubject() == null)
            throw new RuntimeException();

        return verifier.getSubject();
    }

    private Instant getExpirationDate() {
        return LocalDateTime.now().plusHours(jwtExpirationTime).toInstant(ZoneOffset.of("-06:00"));
    }

    public String getUserEmail(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null)
            return null;

        String token = authHeader.replace("Bearer ", "");

        return getSubject(token);
    }

}
