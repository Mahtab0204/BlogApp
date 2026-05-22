package com.kgm.restful_web_services.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {


    @Value("${app.jwt-secret}")
    private String jwtSecretKey;

    @Value("${app.jwt-expiration-milliseconds}")
    private Long jwtExpirationDate;


    // generate JWT Token

    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expiryDate = new Date(currentDate.getTime()+jwtExpirationDate);
        String token = Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(expiryDate)
                .signWith(key())
                .compact();

        return token;
    }


    private Key key(){
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes());
    }

    // get username from JWT Token

    public String getUsername(String token){

        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }


    // validate JWT token

    public boolean validateToken(String token){

        try{
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parse(token);

            return true;
        }catch (MalformedJwtException malformedJwtException){
            throw new BadCredentialsException("Invalid JWT Token");
        }catch (ExpiredJwtException expiredJwtException){
            throw new BadCredentialsException("Expired JWT Token");
        }catch (UnsupportedJwtException unsupportedJwtException){
            throw new BadCredentialsException("Unsupported JWT token.");
        }catch (IllegalArgumentException illegalArgumentException){
            throw new BadCredentialsException("Jwt claims string is null or empty");
        }

    }

}
