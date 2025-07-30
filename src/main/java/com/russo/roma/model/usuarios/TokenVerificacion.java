package com.russo.roma.model.usuarios;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class TokenVerificacion {
    private Integer id;
    private UUID token;
    private Integer usuarioId;
    private LocalDateTime fechaExpiracion;
    private String tipo;
}
