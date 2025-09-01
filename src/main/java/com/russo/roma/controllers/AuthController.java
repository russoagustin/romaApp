package com.russo.roma.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.russo.roma.dto.PeticionLoginDto;
import com.russo.roma.dto.RespuestaLoginDto;
import com.russo.roma.utils.JwtUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthenticationManager authManager;

    private JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authManager, JwtUtils jwtUtils){
        this.authManager = authManager;
        this.jwtUtils = jwtUtils;
    }

    @Operation(
        summary = "Inicio de sesión",
        description = "Permite a un usuario iniciar sesión con su email y contraseña"
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Inicio de sesión exitoso, devuelve un token JWT",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RespuestaLoginDto.class)
                )
            ),

            @ApiResponse(
                responseCode = "401",
                description = "El usuario no pudo iniciar sesión",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Map.class),
                    examples =
                    {
                        @ExampleObject (
                            name = "Credenciales Inválidas",
                            value = "{\"error\": \"Correo o contraseña incorrectos\"}"
                        ),
                        @ExampleObject (
                            name = "Credenciales válidas pero usuario no activado",
                            value = "{\"error\": \"Usuario no activado\"}"
                        )        
                    }
                )
            )
        }
    )
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
