package com.russo.roma.services.interfaces;

import java.util.List;

import com.russo.roma.model.negocio.LineaComanda;

public interface ILineaComandaService {
    LineaComanda buscarPorId(Integer id);
    List<LineaComanda> listarLineasComanda(Integer comandaId);
    void servirLineaComanda(Integer id);
    void borrarLinea(Integer id);
}
