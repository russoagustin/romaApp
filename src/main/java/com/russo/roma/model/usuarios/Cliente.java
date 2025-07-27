package com.russo.roma.model.usuarios;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Cliente extends Usuario{

    /***
     * Atributo que representa los puntos acumulados por el cliente
     * Estos se pueden cambiar por cupones de descuento.
     */
    private Integer puntos;

    public Cliente() {
        super();
    }

    public Cliente(Integer id, String nombres, String apellidos, String email, String password, LocalDate fechaNacimiento, boolean activo, Integer puntos) {
        super(id, nombres, apellidos, email, password, fechaNacimiento, activo);
        this.puntos = puntos;
    }

    public Cliente(Usuario u, Integer puntos){
        super(u.getId(),
            u.getNombres(),
            u.getApellidos(), 
            u.getEmail(), 
            u.getPassword(), 
            u.getFechaNacimiento(), 
            u.isActivo()
        );

        this.puntos = puntos;
        
    }

    public Integer getPuntos() {
        return puntos;
    }

    public void sumarPuntos(Integer p){
        this.puntos += p;
    }
}
