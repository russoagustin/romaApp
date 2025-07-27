package com.russo.roma.model.usuarios;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Administrador extends Usuario{
    
    public Administrador(){

    }

    public Administrador(Integer id, String nombres, String apellidos, String email, String password, LocalDate fechaNacimiento, boolean activo) {
        super(id, nombres, apellidos, email, password, fechaNacimiento, activo);
    }
}
