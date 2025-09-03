package com.russo.roma.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.russo.roma.exceptions.RecursoNoEncontradoException;
import com.russo.roma.model.negocio.EstadoLineaComanda;
import com.russo.roma.model.negocio.LineaComanda;
import com.russo.roma.repositories.impl.LineaComandaRepository;
import com.russo.roma.services.interfaces.ILineaComandaService;

@Service
public class LineaComandaService implements ILineaComandaService{

    private final LineaComandaRepository lineaRepository;

    private static final String LINEA_NO_ENCONTRADA = "Linea de comanda no encontrada";

    public LineaComandaService(LineaComandaRepository lineaRepository){
        this.lineaRepository = lineaRepository;
    }

    @Override
    public LineaComanda buscarPorId(Integer id) {
        return lineaRepository.buscarPorId(id).orElseThrow(()-> new RecursoNoEncontradoException(LINEA_NO_ENCONTRADA));
    }

    @Override
    public List<LineaComanda> listarLineasComanda(Integer comandaId) {
        return lineaRepository.listarLineasComanda(comandaId);
    }

    @Override
    @Transactional
    public void servirLineaComanda(Integer id) {
        LineaComanda linea = lineaRepository.buscarPorId(id)
            .orElseThrow(()-> new RecursoNoEncontradoException(LINEA_NO_ENCONTRADA));
        linea.setEstado(EstadoLineaComanda.SERVIDO);

        lineaRepository.modificarLinea(linea);
    }

    @Override
    @Transactional
    public void borrarLinea(Integer id) {
        lineaRepository.borrarLinea(id);
    }

}
