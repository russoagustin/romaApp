package com.russo.roma.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.russo.roma.model.dto.UsuarioDTO;
import com.russo.roma.model.usuarios.Administrador;
import com.russo.roma.model.usuarios.Cliente;
import com.russo.roma.model.usuarios.Mozo;
import com.russo.roma.model.usuarios.TokenVerificacion;
import com.russo.roma.model.usuarios.Usuario;
import com.russo.roma.repositories.IGestor;
import com.russo.roma.repositories.IUsuarioRepository;
import com.russo.roma.repositories.TokenVerificacionRepository;

@Service
public class UsuarioService implements IUsuarioServices{

    private IUsuarioRepository usuarioRepository;
    private TokenVerificacionRepository tokenRepo;

    
    private emailService email;
    
    private IGestor<Cliente, Integer> clienteRepository;

    private IGestor<Mozo, Integer> mozoRepository;

    private IGestor<Administrador, Integer> adminRepository;

    public UsuarioService(IUsuarioRepository usuarioRepository, 
                    TokenVerificacionRepository tokenRepo,
                    emailService email,
                    @Qualifier("clienteRepository") IGestor<Cliente, Integer> clienteRepository,
                    @Qualifier("mozoRepository") IGestor<Mozo, Integer> mozoRepository,
                    @Qualifier("administradorRepository") IGestor<Administrador, Integer> adminRepository) {
                
        this.usuarioRepository = usuarioRepository;
        this.tokenRepo = tokenRepo;
        this.clienteRepository = clienteRepository;
        this.mozoRepository = mozoRepository;
        this.adminRepository = adminRepository;
        this.email = email;
    }

    @Override
    @Transactional
    public void confirmarCuenta(String token) {
        TokenVerificacion tokenVerificacion = tokenRepo.buscarPorToken(token);
        Optional<Usuario> usuarioOpt = usuarioRepository.buscarPorId(tokenVerificacion.getUsuarioId());

        if (usuarioOpt.isPresent() && tokenVerificacion.getToken().toString().equals(token) && tokenVerificacion.getFechaExpiracion().isAfter(LocalDateTime.now())) {
            Usuario usuario = usuarioOpt.get();
            usuario.setActivo(true);
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
        usuarioRepository.buscarPorId(idUsuario).
                        orElseThrow(()-> new IllegalArgumentException("No se encontró el usuario"));
        Administrador admin = new Administrador();
        admin.setId(idUsuario);
        adminRepository.alta(admin);
    }

    /**
     * Añade a un usuario el rol de Mozo
     */
    @Override
    public void hacerMozo(Integer idUsuario) {
        usuarioRepository.buscarPorId(idUsuario)
                        .orElseThrow(()-> new IllegalArgumentException("No se encontró el usuario"));
        Mozo mozo = new Mozo();
        mozo.setId(idUsuario);
        mozoRepository.alta(mozo);

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
    @Transactional
    public Integer altaUsuario(Usuario usuario) {
        usuario.setActivo(false);
        Integer usuarioId = usuarioRepository.alta(usuario);

        // agrego el rol cliente al nuevo usuario
        Cliente c = new Cliente();
        c.setId(usuarioId);
        c.setPuntos(0);
        clienteRepository.alta(c);

        // Inserto el token para confirmar la cuenta por email
        TokenVerificacion token = new TokenVerificacion();
        token.setToken(UUID.randomUUID());
        token.setFechaExpiracion(LocalDateTime.now().plusHours(24));
        token.setUsuarioId(usuarioId);
        tokenRepo.altaToken(token);

        // Enviar email con el link al endpoint para confirmar la cuenta
        // contenido del email provisorio para hacer pruebas
        String contenido= "http://localhost:8080/api/usuarios/confirmar-email?token=" + token.getToken().toString();
        email.enviarEmail(usuario.getEmail(), "Confirmar Cuenta", contenido);
        
        return usuarioId;
    }

}
