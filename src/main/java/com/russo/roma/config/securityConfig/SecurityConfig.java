package com.russo.roma.config.securityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.russo.roma.config.filters.JwtTokenValidator;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {


    private final AuthenticationConfiguration authenticationConfiguration;

    private final JwtTokenValidator jwtTokenValidator;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JwtTokenValidator jwtTokenValidator){
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtTokenValidator = jwtTokenValidator;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, CustomAccessDeniedHandler accessDeniedHandler, CustomAuthenticationEntryPoint authEntryPoint) throws Exception{
        return httpSecurity
            .csrf(csrf->csrf.disable())
            .httpBasic(Customizer.withDefaults())
            .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(http -> {
                http
                    .anyRequest().permitAll();
            })
            .exceptionHandling(ex -> 
                ex
                    .accessDeniedHandler(accessDeniedHandler)
                    .authenticationEntryPoint(authEntryPoint)
            )

            .addFilterBefore(jwtTokenValidator, BasicAuthenticationFilter.class)
            .build();
    }

    @Bean
    AuthenticationManager authenticationManager() throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    AuthenticationProvider authenticationProvider(UserDetailsService details){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(details);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }
}
