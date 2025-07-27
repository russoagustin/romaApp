package com.russo.roma.repositories;

import java.util.Optional;

public interface IGestor <T,ID>{

    public Optional<T> buscarPorId(ID id);
    public ID alta(T t);
    public void borrar(T t);
    public void modificar(T t);
    
}
