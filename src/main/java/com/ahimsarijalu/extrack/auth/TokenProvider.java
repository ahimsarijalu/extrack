//package com.ahimsarijalu.extrack.auth;
//
//import com.ahimsarijalu.extrack.user.User;
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.exceptions.JWTCreationException;
//import com.auth0.jwt.exceptions.JWTVerificationException;
//import com.auth0.jwt.exceptions.TokenExpiredException;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.time.Instant;
//import java.time.LocalDateTime;
//import java.time.ZoneOffset;
//
//@Service
//public class TokenProvider {
//    @Value("${security.jwt.token.secret-key}")
//    private String JWT_SECRET;
//
//    public String generateAccessToken(User user) {
//        try {
//            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
//            return JWT.create()
//                    .withSubject(user.getUsername())
//                    .withClaim("username", user.getUsername())
//                    .withExpiresAt(genAccessExpirationDate())
//                    .sign(algorithm);
//        } catch (Exception e) {
//            throw new RuntimeException("Error while generating token: " + e.getMessage(), e);
//        }
//    }
//
//    public String validateToken(String token) {
//        try {
//            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
//            var verifier = JWT.require(algorithm)
//                    .withClaimPresence("username") // Ensure the 'username' claim is present
//                    .build();
//
//            var jwt = verifier.verify(token);
//            String username = jwt.getClaim("username").asString();
//
//            if (username == null || username.isEmpty()) {
//                throw new JWTVerificationException("Invalid username claim");
//            }
//
//            return username;
//        } catch (TokenExpiredException e) {
//            throw new TokenExpiredException("Token has expired", e.getExpiredOn());
//        } catch (JWTVerificationException e) {
//            throw new JWTVerificationException("Invalid token", e);
//        }
//    }
//
//
//    private Instant genAccessExpirationDate() {
//        return LocalDateTime.now()
//                .plusHours(24)
//                .toInstant(ZoneOffset.of("+07:00"));
//    }
//}
