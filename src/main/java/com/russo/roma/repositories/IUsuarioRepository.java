package com.russo.roma.repositories;

import java.util.List;
import java.util.Optional;

import com.russo.roma.model.usuarios.Usuario;


public interface IUsuarioRepository{

    public Optional<Usuario> buscarPorId(Integer id);
    public List<Usuario> buscarPorNombre(String nombre);
    public Integer alta(Usuario usuario);
    public void borrar(Usuario usuario);
    public void modificar(Usuario usuario);

}
