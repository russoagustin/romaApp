package com.russo.roma.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.russo.roma.dto.RespuestaLoginDto;
import com.russo.roma.utils.JwtUtils;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("log-in")
    public ResponseEntity<RespuestaLoginDto> iniciarSesion(@RequestBody PeticionLoginDto peticionLogin ){
        Authentication authentication = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(peticionLogin.email(), peticionLogin.password())
        );

        String email = ((UserDetails)authentication.getPrincipal()).getUsername();
        String token = jwtUtils.crearToken(authentication);

        return ResponseEntity.ok(new RespuestaLoginDto(email,token));
    }

}
