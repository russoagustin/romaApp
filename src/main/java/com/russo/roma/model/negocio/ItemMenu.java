package com.russo.roma.model.negocio;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemMenu {

    private Integer id;
    private String nombre;
    private BigDecimal precio;
    /**
     * Puntos que se suman al cliente al comprar este item.
     */
    private Integer puntos;
    private String ingredientes;

    public ItemMenu() {
    }
    public ItemMenu(Integer id, String nombre, BigDecimal precio, Integer puntos, String ingredientes) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.puntos = puntos;
        this.ingredientes = ingredientes;
    }

}
