package com.github.Ahmed_Zein.dms.services;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.github.Ahmed_Zein.dms.models.LocalUser;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {

    @Value("${jwt.algorithm.key}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expiryInSeconds}")
    private Long expiryInMillis;

    private Algorithm algorithm;
    private static final String EMAIL_KEY = "EMAIL_KEY";

    @PostConstruct
    public void postConstruct() {
        this.algorithm = Algorithm.HMAC256(secret);
    }

    public String generateToken(LocalUser user) {
        return JWT.create()
                .withIssuer(issuer)
                .withClaim(EMAIL_KEY, user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiryInMillis * 1000L))
                .sign(algorithm);
    }

    public String getUserEmail(String token) {
        var decode = JWT.require(algorithm).withIssuer(issuer).build().verify(token);
        return decode.getClaim(EMAIL_KEY).asString();
    }

}
