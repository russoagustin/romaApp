package com.russo.roma.services.interfaces;

import java.util.List;

import com.russo.roma.model.negocio.Mesa;

public interface IMesaService {
    Integer altaMesa(Mesa mesa);
    List<Mesa> listarMesas();
    Mesa buscarPorNumero(Integer num);
    void borrarMesa(Integer num);
    void modificarMesa(Integer num, Mesa mesa);
}
