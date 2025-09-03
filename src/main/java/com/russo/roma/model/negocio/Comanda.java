package com.russo.roma.model.negocio;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comanda {
    
    private Integer id;
    private Integer mesa;
    private Integer mozoId;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private EstadoComanda estado;

    private List<LineaComanda> lineasComanda;
}
