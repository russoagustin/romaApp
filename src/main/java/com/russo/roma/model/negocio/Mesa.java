package com.russo.roma.model.negocio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Mesa {
    private Integer numero;
    private String ubicacion;
    private Boolean activa;
}
