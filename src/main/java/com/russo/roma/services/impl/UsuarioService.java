package com.russo.roma.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.russo.roma.dto.UsuarioDTO;
import com.russo.roma.exceptions.RecursoNoEncontradoException;
import com.russo.roma.model.usuarios.Administrador;
import com.russo.roma.model.usuarios.Cliente;
import com.russo.roma.model.usuarios.Mozo;
import com.russo.roma.model.usuarios.TokenVerificacion;
import com.russo.roma.model.usuarios.Usuario;
import com.russo.roma.repositories.impl.TokenVerificacionRepository;
import com.russo.roma.repositories.interfaces.IGestor;
import com.russo.roma.repositories.interfaces.ITokenVerificacionRepository;
import com.russo.roma.repositories.interfaces.IUsuarioRepository;
import com.russo.roma.services.interfaces.IEmailService;
import com.russo.roma.services.interfaces.IUsuarioServices;


@Service
public class UsuarioService implements IUsuarioServices{

    private final IUsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    private final ITokenVerificacionRepository tokenRepo;

    
    private final IEmailService emailService;
    
    private final IGestor<Cliente, Integer> clienteRepository;

    private final IGestor<Mozo, Integer> mozoRepository;

    private final IGestor<Administrador, Integer> adminRepository;

    private static final String MENSAJE_NO_ENCONTRADO_USUARIO = "No se encontró el usuario";
    private static final String MENSAJE_NO_ENCONTRADO_TOKEN = "No se encontró el token";

    @Value("${russo.app.dominio}")
    private String DOMINIO;

    public UsuarioService(IUsuarioRepository usuarioRepository, 
                    PasswordEncoder passwordEncoder,
                    TokenVerificacionRepository tokenRepo,
                    IEmailService email,
                    @Qualifier("clienteRepository") IGestor<Cliente, Integer> clienteRepository,
                    @Qualifier("mozoRepository") IGestor<Mozo, Integer> mozoRepository,
                    @Qualifier("administradorRepository") IGestor<Administrador, Integer> adminRepository) {
                
        this.usuarioRepository = usuarioRepository;
        this.tokenRepo = tokenRepo;
        this.clienteRepository = clienteRepository;
        this.mozoRepository = mozoRepository;
        this.adminRepository = adminRepository;
        this.emailService = email;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void confirmarCuenta(String token) {
        TokenVerificacion tokenVerificacion = tokenRepo.buscarPorToken(token)
            .orElseThrow(()-> new RecursoNoEncontradoException(MENSAJE_NO_ENCONTRADO_TOKEN));

        Usuario usuario = usuarioRepository.buscarPorId(tokenVerificacion.getUsuarioId()).orElseThrow();

        if (tokenVerificacion.getTipo().equals("VERIFICACION_EMAIL") && 
            !tokenVerificacion.getUsado()
        ) 
        {
            tokenVerificacion.setUsado(true);
            tokenRepo.marcarComoUsado(tokenVerificacion);   //marcamos el token como usado.
            usuario.setActivo(true);
            usuarioRepository.modificar(usuario);           //activo el usuario
        }
    }

    @Override
    public void darBajaUsuario(Integer idUsuario) {
        Usuario usuario = usuarioRepository.buscarPorId(idUsuario).
                            orElseThrow(()-> new RecursoNoEncontradoException(MENSAJE_NO_ENCONTRADO_USUARIO));
        
        usuario.setActivo(false);
        usuarioRepository.modificar(usuario);
    }

    @Override
    @Transactional
    public void restablecerContrasena(String token, String contrasena) {
        TokenVerificacion tokenVerificacion = tokenRepo.buscarPorToken(token)
            .orElseThrow(()-> new RecursoNoEncontradoException(MENSAJE_NO_ENCONTRADO_TOKEN));

        Usuario usuario = usuarioRepository.buscarPorId(tokenVerificacion.getUsuarioId()).orElseThrow();

        if (tokenVerificacion.getFechaExpiracion().isAfter(LocalDateTime.now()) &&
            tokenVerificacion.getTipo().equals("RESET_CONTRASENA") &&
            !tokenVerificacion.getUsado()
        ) 
        {
            usuario.setPassword(contrasena);
            usuarioRepository.modificar(usuario);
        }
    }

    @Override
    public void activarUsuario(Integer idUsuario) {
        Usuario usuario = usuarioRepository.buscarPorId(idUsuario).
                            orElseThrow(()-> new RecursoNoEncontradoException(MENSAJE_NO_ENCONTRADO_USUARIO));
        usuario.setActivo(true);
        usuarioRepository.modificar(usuario);
    }
    
    /**
     * Añade a un usuario el rol de Mozo
     */
    @Override
    public void hacerAdmin(Integer idUsuario) {
        usuarioRepository.buscarPorId(idUsuario).
                        orElseThrow(()-> new RecursoNoEncontradoException(MENSAJE_NO_ENCONTRADO_USUARIO));
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
                        .orElseThrow(()-> new RecursoNoEncontradoException(MENSAJE_NO_ENCONTRADO_USUARIO));
        Mozo mozo = new Mozo();
        mozo.setId(idUsuario);
        mozoRepository.alta(mozo);

    }

    @Override
    public UsuarioDTO buscarUsuarioPorId(Integer idUsuario) {
        Usuario usuario = usuarioRepository.buscarPorId(idUsuario)
            .orElseThrow(()-> new RecursoNoEncontradoException(MENSAJE_NO_ENCONTRADO_USUARIO));
        
        UsuarioDTO usuarioDto = new UsuarioDTO(usuario.getId(),
                                    usuario.getNombres(),
                                    usuario.getApellidos(),
                                    usuario.getEmail(),
                                    usuario.isActivo());
        
        return usuarioDto;
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
    public void borrarUsuario(Integer id) {
        Usuario u = usuarioRepository.buscarPorId(id)
            .orElseThrow(()-> new IllegalArgumentException(MENSAJE_NO_ENCONTRADO_USUARIO));
        usuarioRepository.borrar(u);
    }

    @Override
    public void modificarUsuario(Integer id,Usuario usuario) {
        Usuario u = usuarioRepository.buscarPorId(id)
            .orElseThrow(()-> new IllegalArgumentException(MENSAJE_NO_ENCONTRADO_USUARIO));
        
        usuario.setId(u.getId());
        usuario.setPassword(u.getPassword()); //evita que se pueda modificar la contraseña
        usuario.setActivo(u.isActivo()); //evita que se pueda modificar el estado.
    
        usuarioRepository.modificar(usuario);
    }

    @Override
    @Transactional
    public Integer altaUsuario(Usuario usuario) {
        usuario.setActivo(false);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        Integer usuarioId = usuarioRepository.alta(usuario);

        // agrego el rol cliente al nuevo usuario
        Cliente c = new Cliente();
        c.setId(usuarioId);
        c.setPuntos(0);
        clienteRepository.alta(c);

        // Inserto el token para confirmar la cuenta por email
        TokenVerificacion token = new TokenVerificacion();
        token.setToken(UUID.randomUUID());
        token.setFechaExpiracion(null);
        token.setUsuarioId(usuarioId);
        token.setTipo("VERIFICACION_EMAIL");
        tokenRepo.altaToken(token);

        // Enviar email con el link al endpoint para confirmar la cuenta
        // contenido del email provisorio para hacer pruebas
        String contenido = DOMINIO + "/api/usuarios/confirmar-email?token=" + token.getToken().toString();
        emailService.enviarEmail(usuario.getEmail(), "Confirmar Cuenta", contenido);
        
        return usuarioId;
    }

    @Override
    @Transactional
    public void crearTokenCambioContrasena(String emailusuario){
        Usuario usuario = usuarioRepository.buscarPorEmail(emailusuario)
            .orElseThrow(()->new RecursoNoEncontradoException("El email no está registrado"));

        TokenVerificacion token = new TokenVerificacion();
        token.setFechaExpiracion(LocalDateTime.now().plusMinutes(30));
        token.setUsuarioId(usuario.getId());
        token.setTipo("RESET_CONTRASENA");
        token.setToken(UUID.randomUUID());
        tokenRepo.altaToken(token);

        String contenido = "Haz click en el siguiente link para cambiar tu contraseña";
        emailService.enviarEmail(emailusuario, "Cambio Contraseña", contenido);
        
    }
}
