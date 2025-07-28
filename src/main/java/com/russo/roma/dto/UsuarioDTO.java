package com.russo.roma.dto;

public record UsuarioDTO(
    Integer id,
    String nombres,
    String apellidos,
    String email,
    Boolean activo) {

}
