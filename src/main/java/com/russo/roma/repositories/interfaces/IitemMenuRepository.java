package com.russo.roma.repositories.interfaces;

import java.util.List;
import java.util.Optional;

import com.russo.roma.model.negocio.ItemMenu;

public interface IitemMenuRepository {
    public Optional<ItemMenu> buscarPorId(Integer id);
    public List<ItemMenu> buscarPorNombre(String nombre);
    public Integer alta(ItemMenu itemMenu);
    public void modificar(ItemMenu itemMenu);
    public void borrar(ItemMenu itemMenu);
}
