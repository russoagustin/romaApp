package com.russo.roma.config.filters;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.russo.roma.model.usuarios.CustomUserDetails;
import com.russo.roma.services.impl.UserDetailsService;
import com.russo.roma.utils.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtTokenValidator extends OncePerRequestFilter{


    private final JwtUtils jwtUtils;

    private final UserDetailsService detailsService;

    public JwtTokenValidator(JwtUtils jwtUtils, UserDetailsService detailsService){
        this.jwtUtils = jwtUtils;
        this.detailsService = detailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, 
                                    @NonNull HttpServletResponse response, 
                                    @NonNull FilterChain filterChain) throws ServletException, IOException 
    {
        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            jwtToken = jwtToken.substring(7);
            DecodedJWT decodedJWT;

            try {
                decodedJWT = jwtUtils.validarToken(jwtToken);    
            } catch (JWTVerificationException e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
                return;
            }
            
            String email = jwtUtils.obtenerEmail(decodedJWT);
            CustomUserDetails userDetails = detailsService.loadUserByUsername(email);

            SecurityContext context = SecurityContextHolder.getContext();
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());

            context.setAuthentication(authentication);

            SecurityContextHolder.setContext(context);

        }

        filterChain.doFilter(request, response);
    }

}
