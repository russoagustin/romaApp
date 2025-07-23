package com.russo.roma.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.russo.roma.model.dto.UsuarioDTO;
import com.russo.roma.model.usuarios.Rol;
import com.russo.roma.model.usuarios.TokenVerificacion;
import com.russo.roma.model.usuarios.Usuario;
import com.russo.roma.repositories.IUsuarioRepository;
import com.russo.roma.repositories.TokenVerificacionRepository;

@Service
public class UsuarioService implements IUsuarioServices{

    private IUsuarioRepository usuarioRepository;
    private TokenVerificacionRepository tokenRepo;

    public UsuarioService(IUsuarioRepository usuarioRepository, TokenVerificacionRepository tokenRepo) {
        this.usuarioRepository = usuarioRepository;
        this.tokenRepo = tokenRepo;
    }

    @Override
    @Transactional
    public void confirmarCuenta(Integer usuarioId, String token) {
        TokenVerificacion tokenVerificacion = tokenRepo.buscarPorUsuarioId(usuarioId);
        Optional<Usuario> usuarioOpt = usuarioRepository.buscarPorId(usuarioId);

        if (usuarioOpt.isPresent() && tokenVerificacion.getToken().toString().equals(token)) {
            Usuario usuario = usuarioOpt.get();
            usuario.setActivo(true);
            usuario.setRoles(null);
            usuarioRepository.modificar(usuario);
        }
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
        Optional<Usuario> usuarioOpt = usuarioRepository.buscarPorId(usuario.getId());
        Usuario u = usuarioOpt.orElseThrow();

        usuario.setActivo(u.isActivo()); //evita que se pueda modificar el estado.

        usuarioRepository.modificar(usuario);
    }

    @Override
    public void altaUsuario(Usuario usuario) {
        usuario.setActivo(false);
        Integer usuarioId = usuarioRepository.alta(usuario);
        
        TokenVerificacion token = new TokenVerificacion();
        token.setToken(UUID.randomUUID());
        token.setFechaExpiracion(LocalDateTime.now().plusHours(24));
        token.setUsuarioId(usuarioId);
        
        tokenRepo.altaToken(token);
        
    }

}
