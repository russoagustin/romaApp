package com.russo.roma.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.russo.roma.exceptions.RecursoNoEncontradoException;
import com.russo.roma.model.negocio.ItemMenu;
import com.russo.roma.repositories.interfaces.IitemMenuRepository;
import com.russo.roma.services.interfaces.IitemMenuService;

@Service
public class ItemMenuService implements IitemMenuService{

    private final IitemMenuRepository itemRepo;

    private String MENSAJE_NO_ENCONTRADO = "No se encontró el item de menú";

    public ItemMenuService(IitemMenuRepository itemRepo){
        this.itemRepo = itemRepo;
    }

    @Override
    public ItemMenu buscarPorId(Integer id) {
        return itemRepo.buscarPorId(id)
            .orElseThrow(()-> new RecursoNoEncontradoException(MENSAJE_NO_ENCONTRADO));
    }

    @Override
    public List<ItemMenu> buscarPorNombre(String nombre) {
        return itemRepo.buscarPorNombre(nombre);
    }

    @Override
    public Integer altaItemMenu(ItemMenu item) {
        return itemRepo.alta(item);
    }

    @Override
    public void borrarItemMenu(Integer id) {
        ItemMenu item = itemRepo.buscarPorId(id)
            .orElseThrow(()-> new RecursoNoEncontradoException(MENSAJE_NO_ENCONTRADO));
        itemRepo.borrar(item);;
    }

    @Override
    public void modificarItemMenu(Integer id, ItemMenu nuevoItem) {
        ItemMenu i = itemRepo.buscarPorId(id)
            .orElseThrow(()-> new RecursoNoEncontradoException(MENSAJE_NO_ENCONTRADO));

        nuevoItem.setId(i.getId());
        itemRepo.modificar(nuevoItem);
    
    }

    @Override
    public void ponerNoDisponible(Integer id) {
        ItemMenu item = itemRepo.buscarPorId(id)
            .orElseThrow(()-> new RecursoNoEncontradoException(MENSAJE_NO_ENCONTRADO));
        if (item.getDisponible()) {
            item.setDisponible(false);
            itemRepo.modificar(item);
        }
    }

    @Override
    public void ponerDisponible(Integer id) {
        ItemMenu item = itemRepo.buscarPorId(id)
            .orElseThrow(()-> new RecursoNoEncontradoException(MENSAJE_NO_ENCONTRADO));
        if (!item.getDisponible()) {
            item.setDisponible(true); 
            itemRepo.modificar(item);
        }
    }

}
