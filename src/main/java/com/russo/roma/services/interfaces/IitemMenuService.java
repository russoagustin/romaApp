package com.russo.roma.services.interfaces;

import java.util.List;

import com.russo.roma.model.negocio.ItemMenu;

public interface IitemMenuService {
    
    ItemMenu buscarPorId(Integer id);
    List<ItemMenu> buscarPorNombre(String nombre);
    Integer altaItemMenu(ItemMenu item);
    void modificarItemMenu(Integer id, ItemMenu item);
    void borrarItemMenu(Integer id);
    
    void ponerNoDisponible(Integer id);
    void ponerDisponible(Integer id);
}
