package com.russo.roma.model.negocio;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@AllArgsConstructor
@Getter @Setter
public class ItemMenu {

    private Integer id;
    private String nombre;
    private BigDecimal precio;
    private CategoriaItemMenu categoria;
    /**
     * Puntos que se suman al cliente al comprar este item.
     */
    private Integer puntos;
    private String ingredientes;
    private String descripcion;
    private Boolean disponible;
    
    public ItemMenu() {
    }
    public ItemMenu(Integer id, String nombre, BigDecimal precio, Integer puntos, String ingredientes, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.puntos = puntos;
        this.ingredientes = ingredientes;
    }

}
