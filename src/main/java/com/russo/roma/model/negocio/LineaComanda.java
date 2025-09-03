package com.russo.roma.model.negocio;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class LineaComanda {

    private Integer id;
    private Integer comandaId;
    private ItemMenu item;
    private Integer cantidad;
    private BigDecimal precio;
    private EstadoLineaComanda estado;
}
