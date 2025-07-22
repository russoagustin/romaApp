package com.russo.roma.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.russo.roma.model.dto.UsuarioDTO;
import com.russo.roma.model.usuarios.Rol;
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
    public void darBajaUsuario(Integer idUsuario) {
        Usuario usuario = usuarioRepository.buscarPorId(idUsuario).
                            orElseThrow(()-> new IllegalArgumentException("No se encontró el usuario"));
        
        usuario.setActivo(false);
        usuarioRepository.modificar(usuario);
    }

    @Override
    public void restablecerContrasena(String email, String contrasena) {
        //TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'restablecerContrasena'");
    }

    @Override
    public void activarUsuario(Integer idUsuario) {
        Usuario usuario = usuarioRepository.buscarPorId(idUsuario).
                            orElseThrow(()-> new IllegalArgumentException("No se encontró el usuario"));
        usuario.setActivo(true);
        usuarioRepository.modificar(usuario);
    }
    
    /**
     * Añade a un usuario el rol de Mozo
     */
    @Override
    public void hacerAdmin(Integer idUsuario) {
        Usuario usuario = usuarioRepository.buscarPorId(idUsuario).
                            orElseThrow(()-> new IllegalArgumentException("No se encontró el usuario"));
        List<Rol> roles  = List.of(Rol.ROLE_ADMIN);
        //metodo modificar solamente puede agregar roles, no quitarlos
        //por esa razon la lista de roles solo contiene el rol ADMIN.
        usuario.setRoles(roles);
        usuarioRepository.modificar(usuario);
    }

    /**
     * Añade a un usuario el rol de Mozo
     */
    @Override
    public void hacerMozo(Integer idUsuario) {
        Usuario usuario = usuarioRepository.buscarPorId(idUsuario)
                                    .orElseThrow(()-> new IllegalArgumentException("No se encontró el usuario"));
        //metodo modificar solamente puede agregar roles, no quitarlos
        //por esa razon la lista de roles solo contiene el rol MOZO.
        List<Rol> roles = List.of(Rol.ROLE_MOZO);
        usuario.setRoles(roles);
        usuarioRepository.modificar(usuario);
        
    }

    @Override
    public Optional<UsuarioDTO> buscarUsuarioPorId(Integer idUsuario) {
        Optional<Usuario> usuarioOpt = usuarioRepository.buscarPorId(idUsuario);
        UsuarioDTO usuarioDto = null;
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuarioDto = new UsuarioDTO(usuario.getId(),
                                        usuario.getNombres(),
                                        usuario.getApellidos(),
                                        usuario.getEmail(),
                                        usuario.isActivo());
        }
        return Optional.ofNullable(usuarioDto);
    }

    @Override
    public List<UsuarioDTO> buscarUsuarioPorNombre(String nombre) {
        List<UsuarioDTO> listaDtos = new ArrayList<>();
        List<Usuario> listaUsuarios =  usuarioRepository.buscarPorNombre(nombre);
        listaUsuarios.forEach(u->{
            listaDtos.add(new UsuarioDTO(u.getId(),u.getNombres(),u.getApellidos(),u.getEmail(),u.isActivo()));
        });
        return listaDtos;
    }

    @Override
    public void borrarUsuario(Usuario usuario) {
        usuarioRepository.borrar(usuario);
    }

    @Override
    public void modificarUsuario(Usuario usuario) {
        usuarioRepository.modificar(usuario);
    }

    @Override
    public void altaUsuario(Usuario usuario) {
        usuarioRepository.alta(usuario);
    }

}
