package com.russo.roma.model.usuarios;

import java.time.LocalDate;

public class Mozo extends Usuario{

    public Mozo() {
        super();
    }

    public Mozo(Integer id, String nombres, String apellidos, String email, String password, LocalDate fechaNacimiento, boolean activo) {
        super(id, nombres, apellidos, email, password, fechaNacimiento, activo);
    }


}
