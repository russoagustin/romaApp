package com.russo.roma.model.dto;

public record UsuarioDTO(
    Integer id,
    String nombres,
    String apellidos,
    String email,
    Boolean activo) {

}
