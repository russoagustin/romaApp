package com.russo.roma.dto;

import java.time.LocalDateTime;

public record ParametroFaltanteDto(
    String error,
    String mensaje,
    LocalDateTime timestamp
) {

}
