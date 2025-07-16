package com.russo.roma.model.usuarios;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Usuario {

    private Integer id;
    private String nombres;
    private String apellidos;
    private String email;
    private String password;
    /**
     * atributo para los roles del usuario
     */
    private List<String> roles = null;
    private LocalDate fechaNacimiento;
    private boolean activo;

    //----CONSTRUCTORES----
    public Usuario() {
    }
    
    public Usuario(Integer id, String nombres, String apellidos, String email, String password, LocalDate fechaNacimiento, boolean activo) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.password = password;
        this.fechaNacimiento = fechaNacimiento;
        this.activo = activo;
    }


    //----GETTERS----
    public boolean estaActivo(){
        return activo;
    }

    // Resto de getters y setters los hace lombok.

}
