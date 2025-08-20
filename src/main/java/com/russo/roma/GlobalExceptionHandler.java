package com.russo.roma;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.russo.roma.dto.ParametroFaltanteDto;
import com.russo.roma.exceptions.RecursoNoEncontradoException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<String> recursoNoEncontradoHandler(RecursoNoEncontradoException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Void> dataIntegrityViolationHandler(){
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ParametroFaltanteDto> missigServletRequestParameterHandler(MissingServletRequestParameterException ex){
        ParametroFaltanteDto body = new ParametroFaltanteDto(
            "Parametro requerido faltante.",
            "Falta el parámetro: " + ex.getParameterName(),
            LocalDateTime.now()
        );
        return ResponseEntity.badRequest().headers(ex.getHeaders()).body(body);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Correo o contraseña inválidos"));
    }
}
