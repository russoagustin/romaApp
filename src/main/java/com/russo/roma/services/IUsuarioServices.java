package com.russo.roma.services;

import java.util.List;
import java.util.Optional;

import com.russo.roma.model.dto.UsuarioDTO;
import com.russo.roma.model.usuarios.Usuario;

public interface IUsuarioServices {

    public void confirmarCuenta(Usuario usuario);
    public void darBajaUsuario(Integer idUsuario);
    public void restablecerContrasena(String email, String contrasena);
    public void activarUsuario(Integer idUsuario);
    public void hacerAdmin(Integer idUsuario);
    public void hacerMozo(Integer idUsuario);

    public Optional<UsuarioDTO> buscarUsuarioPorId(Integer idUsuario);
    public List<UsuarioDTO> buscarUsuarioPorNombre(String nombre);
    public void borrarUsuario(Usuario usuario);
    public void modificarUsuario(Usuario usuario);
    public void altaUsuario(Usuario usuario);

}
