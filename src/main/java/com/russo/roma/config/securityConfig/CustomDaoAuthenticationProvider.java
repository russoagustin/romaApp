package com.russo.roma.config.securityConfig;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;


public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider{

    public CustomDaoAuthenticationProvider(org.springframework.security.core.userdetails.UserDetailsService details){
        super(details);
        setPreAuthenticationChecks(userDetails -> {
           
        });
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  org.springframework.security.authentication.UsernamePasswordAuthenticationToken authentication) {
        if (authentication.getCredentials() == null || 
            !getPasswordEncoder().matches(authentication.getCredentials().toString(), userDetails.getPassword())) {
            throw new BadCredentialsException("Usuario o contraseña incorrectos.");
        }

        if (!userDetails.isEnabled()) {
            throw new DisabledException("El usuario está deshabilitado.");
        }
    }
}
