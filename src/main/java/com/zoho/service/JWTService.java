package com.zoho.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class JWTService {


    //this @Value annotation comes from org.springframework.beans.factory.annotation.Value

    @Value("${jwt.algorithm.key}")
    private String algorithmKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expiry.duration}")
    private int expiryTime;

    private Algorithm algorithm;



    @PostConstruct
    public void postConstruct() {
        // Use getBytes to ensure no encoding issues
        algorithm = Algorithm.HMAC256(algorithmKey.getBytes()); // ✅ this is correct

    }


    public String generateToken(String username) {

        return JWT.create()
                .withClaim("name", username)
                .withExpiresAt(new Date(System.currentTimeMillis() + expiryTime))
                .withIssuer(issuer)
                .sign(algorithm);


    }

    public String getUserName(String token) {

        DecodedJWT decodedJWT =
                JWT.
                        require(algorithm).
                        withIssuer(issuer)
                        .build()
                        .verify(token);

        return decodedJWT.getClaim("name").asString();


    }



}
