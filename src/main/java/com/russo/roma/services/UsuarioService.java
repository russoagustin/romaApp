package com.russo.roma.services;

import java.util.List;
import java.util.Optional;

import com.russo.roma.dtos.UsuarioDTO;
import com.russo.roma.model.usuarios.Usuario;
import com.russo.roma.repositories.IUsuarioRepository;

public class UsuarioService implements IUsuarioServices{

    private IUsuarioRepository usuarioRepository;

    public UsuarioService(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void confirmarCuenta(Usuario usuario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'confirmarCuenta'");
    }

    @Override
    public void darBajaUsuario(Usuario usuario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'darBajaUsuario'");
    }

    @Override
    public void restablecerContrasena(UsuarioDTO usuarioDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'restablecerContrasena'");
    }

    @Override
    public void activarUsuario(Integer idUsuario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'activarUsuario'");
    }

    @Override
    public void hacerAdmin(Integer idUsuario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hacerAdmin'");
    }

    @Override
    public void hacerMozo(Integer idUsuario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hacerMozo'");
    }

    @Override
    public void sumarPuntos(Integer idUsuario, Integer puntos) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sumarPuntos'");
    }

    @Override
    public void sumarPuntos(Integer idUsuario, String codigo) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sumarPuntos'");
    }

    @Override
    public Optional<Usuario> buscarUsuarioPorId(Integer idUsuario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarUsuarioPorId'");
    }

    @Override
    public List<Usuario> buscarUsuarioPorNombre(String nombre) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarUsuarioPorNombre'");
    }

    @Override
    public void borrarUsuario(Usuario usuario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'borrarUsuario'");
    }

    @Override
    public void modificarUsuario(Usuario usuario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'modificarUsuario'");
    }

    @Override
    public void altaUsuario(Usuario usuario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'altaUsuario'");
    }

}
