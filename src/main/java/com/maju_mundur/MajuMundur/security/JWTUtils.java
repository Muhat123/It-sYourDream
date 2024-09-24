package com.maju_mundur.MajuMundur.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.maju_mundur.MajuMundur.entity.User;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class JWTUtils {
    private static final String JWT_SECRET = "123846235721415790143590134589435797830956895068298439582348058ruthergh894573478534892535612344132";
    public static final String APP_NAME = "MAJUMUNDURCLOTHING";
    public static final long JWT_EXPIRED = 86400000; // 24 hours
    private Algorithm algorithm;

    @PostConstruct
    private void initAlgorithm() {
        this.algorithm = Algorithm.HMAC256(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

    }

    public String generateToken(User user){
        return JWT.create()
                .withIssuer(APP_NAME)
                .withSubject(user.getId())
                .withExpiresAt(Instant.now().plusSeconds(JWT_EXPIRED))
                .withIssuedAt(Instant.now())
                .withClaim("role", user.getRole().getName().name())
                .sign(algorithm);
    }

    public boolean verifyToken(String token){
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getIssuer().equals(APP_NAME);
    }

    public Map<String, String> getUserInfo(String token){
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("userId", decodedJWT.getSubject());
        userInfo.put("role", decodedJWT.getClaim("role").asString());
        return  userInfo;
    }

}
