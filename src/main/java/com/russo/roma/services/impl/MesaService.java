package com.russo.roma.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.russo.roma.exceptions.RecursoNoEncontradoException;
import com.russo.roma.model.negocio.Mesa;
import com.russo.roma.repositories.interfaces.IMesasRepository;
import com.russo.roma.services.interfaces.IMesaService;

@Service
public class MesaService implements IMesaService{

    private static final String NO_ENCONTRADO = "No se encontró la mesa con el numero: ";

    private final IMesasRepository mesasRepo;

    public MesaService(IMesasRepository mesasRepo){
        this.mesasRepo = mesasRepo;
    }

    @Override
    public Integer altaMesa(Mesa mesa) {
       return mesasRepo.alta(mesa);
    }

    @Override
    public List<Mesa> listarMesas() {
        return mesasRepo.listar();
    }

    @Override
    public Mesa buscarPorNumero(Integer num) {
        return mesasRepo.buscarPorNumero(num)
            .orElseThrow(()-> new RecursoNoEncontradoException(NO_ENCONTRADO + num));
    }

    @Override
    public void borrarMesa(Integer num) {
        Mesa mesa = new Mesa();
        mesa.setNumero(num);
        mesasRepo.borrar(mesa);
    }

    @Override
    public void modificarMesa(Integer num, Mesa mesa) {
        mesa.setNumero(num);
        mesasRepo.modificar(mesa);
    }

}
