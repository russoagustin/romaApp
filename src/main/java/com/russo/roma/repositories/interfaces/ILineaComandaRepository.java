package com.russo.roma.repositories.interfaces;

import java.util.List;
import java.util.Optional;

import com.russo.roma.model.negocio.LineaComanda;

public interface ILineaComandaRepository {

    Optional<LineaComanda> buscarPorId(Integer id);
    List<LineaComanda> listarLineasComanda(Integer comandaId);
    Integer nuevaLinea(LineaComanda lineaComanda);
    void modificarLinea(LineaComanda lineaComanda);
    void borrarLinea(Integer id);
}
