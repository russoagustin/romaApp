package com.russo.roma;

import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.russo.roma.dto.ParametroFaltanteDto;
import com.russo.roma.dto.RespuestaGenericaDto;
import com.russo.roma.exceptions.RecursoNoEncontradoException;
import com.russo.roma.exceptions.TokenVerificacionException;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@ControllerAdvice
public class GlobalExceptionHandler {

    public GlobalExceptionHandler(){}

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<RespuestaGenericaDto> recursoNoEncontradoHandler(RecursoNoEncontradoException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RespuestaGenericaDto(ex.getMessage()));
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

    @ExceptionHandler(TokenVerificacionException.class)
    public ResponseEntity<RespuestaGenericaDto> tokenExceptionHandler(TokenVerificacionException ex){
        return ResponseEntity.badRequest().body(new RespuestaGenericaDto(ex.getMessage()));
    }

}
