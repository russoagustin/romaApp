package com.russo.roma.repositories.interfaces;

import java.util.List;
import java.util.Optional;

import com.russo.roma.model.negocio.Mesa;

public interface IMesasRepository {
    List<Mesa> listar();
    Optional<Mesa> buscarPorNumero(Integer numero);
    void modificar(Mesa mesa);
    void borrar(Mesa mesa);
    Integer alta(Mesa mesa);
}
