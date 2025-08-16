package com.russo.roma.utils;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtUtils {

    @Value("${security.jwt.private-key}")
    private String key;

    @Value("${security.jwt.issuer}")
    private String issuer;

    public String crearToken(Authentication auth){

        Algorithm algoritmo = Algorithm.HMAC256(key);
        String email = ((UserDetails)auth.getPrincipal()).getUsername();
        
        String authorities = auth.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        
        String jwt = JWT.create()
                .withIssuer(issuer)
                .withSubject(email)
                .withClaim("authorities", authorities)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis()+1800000))
                .withJWTId(UUID.randomUUID().toString())
                .sign(algoritmo);

        return jwt;
    }

    public DecodedJWT validarToken(String jwt){
        

        try {
            Algorithm algoritmo = Algorithm.HMAC256(key);
            return JWT.require(algoritmo)
                .withIssuer(issuer)
                .build()
                .verify(jwt);
        } catch (TokenExpiredException e) {
            throw new RuntimeException("Token Expirado");
        } catch (JWTVerificationException e){
            throw new RuntimeException("Token Inválido");
        }
        
    }

    public String obtenerEmail(DecodedJWT decodedJWT){
        return decodedJWT.getSubject().toString();
    }
    
    public Claim obtenerAuthorities(DecodedJWT decodedJWT){
        return decodedJWT.getClaim("authorities");
    }

    public Claim obtenerClaim(DecodedJWT decodedJWT, String claimName){
        return decodedJWT.getClaim(claimName);
    }
}
