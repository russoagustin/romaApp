package com.russo.roma.services.interfaces;

import java.util.List;
import java.util.Optional;

import com.russo.roma.dto.UsuarioDTO;
import com.russo.roma.model.usuarios.Usuario;

public interface IUsuarioServices {

    public void confirmarCuenta(String token);
    public void darBajaUsuario(Integer idUsuario);
    public void restablecerContrasena(String token, String contrasena);
    public void activarUsuario(Integer idUsuario);
    public void hacerAdmin(Integer idUsuario);
    public void hacerMozo(Integer idUsuario);

    public Optional<UsuarioDTO> buscarUsuarioPorId(Integer idUsuario);
    public List<UsuarioDTO> buscarUsuarioPorNombre(String nombre);
    public void borrarUsuario(Integer id);
    public void modificarUsuario(Integer id,Usuario usuario);
    public Integer altaUsuario(Usuario usuario);
    public void crearTokenCambioContrasena(String emailusuario);

}
