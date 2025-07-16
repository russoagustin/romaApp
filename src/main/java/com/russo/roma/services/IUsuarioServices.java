package com.russo.roma.services;

import java.util.List;
import java.util.Optional;

import com.russo.roma.dtos.UsuarioDTO;
import com.russo.roma.model.usuarios.Usuario;

public interface IUsuarioServices {

    public void confirmarCuenta(Usuario usuario);
    public void darBajaUsuario(Usuario usuario);
    public void restablecerContrasena(UsuarioDTO usuarioDTO);
    public void activarUsuario(Integer idUsuario);
    public void hacerAdmin(Integer idUsuario);
    public void hacerMozo(Integer idUsuario);
    public void sumarPuntos(Integer idUsuario, Integer puntos);
    public void sumarPuntos(Integer idUsuario, String codigo);

    public Optional<Usuario> buscarUsuarioPorId(Integer idUsuario);
    public List<Usuario> buscarUsuarioPorNombre(String nombre);
    public void borrarUsuario(Usuario usuario);
    public void modificarUsuario(Usuario usuario);
    public void altaUsuario(Usuario usuario);

}
