package com.hexaquiz.security.service;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hexaquiz.dto.tokens.Tokens;
import com.hexaquiz.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class JwtService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${jwt.access-token.expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    public Tokens getJwtUserToken(UserPrincipal user) {
        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);
        return new Tokens(accessToken, refreshToken);
    }

    public String generateAccessToken(UserPrincipal user) {
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("HexaQuiz")
                    .withSubject(user.getUsername())
                    .withClaim("id", user.getId().toString())
                    .withClaim("type", "access_token")
                    .withExpiresAt(genExpiration(accessTokenExpiration))
                    .sign(algorithm);
        }catch (JWTCreationException exception) {
            throw new RuntimeException(exception);
        }
    }

    public String generateRefreshToken(UserPrincipal user) {
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("HexaQuiz")
                    .withSubject(user.getUsername())
                    .withClaim("type", "refresh_token")
                    .withExpiresAt(genExpiration(refreshTokenExpiration))
                    .sign(algorithm);
        }catch (JWTCreationException exception) {
            throw new RuntimeException(exception);
        }
    }

    public String validateToken(String token) {
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
            return jwt.getSubject();
        }catch (JWTVerificationException exception) {
            return "";
        }
    }

    public String getTypeFromToken(String token) {
        return JWT.decode(token).getClaim("type").asString();
    }

    private Instant genExpiration(long milliseconds) {
        return LocalDateTime.now()
                .plusSeconds(milliseconds / 1000)
                .toInstant(ZoneOffset.of("-03:00"));
    }
}
