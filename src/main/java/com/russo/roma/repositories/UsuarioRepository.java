package com.russo.roma.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.russo.roma.model.usuarios.Rol;
import com.russo.roma.model.usuarios.Usuario;

@Repository
public class UsuarioRepository implements IUsuarioRepository{

    //CONSULTAS SQL
    
    //buscar por id
    private static final String BUSQUEDA_ID = "SELECT * FROM usuarios WHERE id = ?";

    // insertar usuario
    private static final String INSERTAR = "INSERT INTO usuarios (nombres,apellidos,email,contrasena,fecha_nac, activo) VALUES (?,?,?,md5(?),?,?)";

    // buscar por nonmbre
    private static final String BUSQUEDA_NOMBRE = "SELECT * FROM usuarios WHERE nombres LIKE ?";

    // borrar usuario
    private static final String BORRAR = "DELETE FROM usuarios WHERE id = ?";

    // modificar usuario
    private static final String MODIFICAR = "UPDATE usuarios SET nombres = ?, apellidos = ?, email = ?, contrasena = md5(?), activo = ?, fecha_nac = ? WHERE id = ?";

    // añadir a la tabla clientes (obtiene el rol CLIENTE)
    private static final String CLIENTE = "INSERT INTO clientes (usuario_id) VALUES (?)";

    // añadir a la tabla mozo (obtiene el rol MOZO)
    private static final String MOZO = "INSERT INTO mozos (usuario_id) VALUES (?)";

    // añadir a la tabla administradores (obtiene el rol ADMIN)
    private static final String ADMIN = "INSERT INTO administradores (usuario_id) VALUES (?)";

    private static final String BUSCAR_EMAIL = "SELECT id FROM usuarios WHERE email = ?";

    private JdbcTemplate jdbcTemplate;
    // inyección de dependencias
    public UsuarioRepository(JdbcTemplate jdbc){
        this.jdbcTemplate = jdbc;
    }

    private RowMapper<Usuario> UsuarioRM = ((rs, rowNum)-> new Usuario(
        rs.getInt("id"),
        rs.getString("nombres"),
        rs.getString("apellidos"),
        rs.getString("email"),
        rs.getString("contrasena"),
        rs.getDate("fecha_nac").toLocalDate(),
        rs.getBoolean("activo")
    ));

    @Override
    public Optional<Usuario> buscarPorId(Integer id) {
        Usuario usuario;

        try {
            usuario = jdbcTemplate.queryForObject(BUSQUEDA_ID, UsuarioRM, id);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            usuario = null;
        }

        // Obtener roles de las tablas clientes, mozos y administradores
        if (usuario != null){
            List<Rol> roles = obtenerRolesPorUsuarioId(id);
            usuario.setRoles(roles);
        }

        return Optional.ofNullable(usuario);
    }

    @Override
    public List<Usuario> buscarPorNombre(String nombre) {
        List<Usuario> usuarios;
        usuarios = jdbcTemplate.query(BUSQUEDA_NOMBRE, UsuarioRM, "%" + nombre + "%");
        // Obtener roles de las tablas clientes, mozos y administradores
        for (Usuario usuario : usuarios) {
            List<Rol> roles = obtenerRolesPorUsuarioId(usuario.getId());
            usuario.setRoles(roles);
        }
        return usuarios;
    }

    // nota: las etiquetas Transactional hacen rollback automáticamente en caso de excepción.
    
    @Override
    @Transactional
    public void alta(Usuario usuario){
        //Si el usuario se inserta sin error cargarlo en la tabla clientes
        jdbcTemplate.update(INSERTAR,
            usuario.getNombres(),
            usuario.getApellidos(),
            usuario.getEmail(),
            usuario.getPassword(),
            usuario.getFechaNacimiento(),
            usuario.isActivo()
        );
        //Obtener el ID del usuario recién insertado
        //Busco por el email que es un campo único
        Integer usuarioId = jdbcTemplate.queryForObject(BUSCAR_EMAIL, Integer.class, usuario.getEmail());
        jdbcTemplate.update(CLIENTE, usuarioId);
    }

    @Override
    @Transactional
    public void borrar(Usuario usuario) throws org.springframework.dao.DataIntegrityViolationException{
        jdbcTemplate.update(BORRAR, usuario.getId());
    }

    @Override
    @Transactional
    public void modificar(Usuario usuario) {
        jdbcTemplate.update(MODIFICAR,
            usuario.getNombres(),
            usuario.getApellidos(),
            usuario.getEmail(),
            usuario.getPassword(),
            usuario.isActivo(),
            usuario.getFechaNacimiento(),
            usuario.getId()
        );

        List<Rol> roles = usuario.getRoles();
        Integer usuarioId = usuario.getId();

        if (roles.contains(Rol.ROLE_CLIENTE) && usuarioId != null) {
            jdbcTemplate.update(CLIENTE, usuarioId);
        }
        if (roles.contains(Rol.ROLE_MOZO) && usuarioId != null) {
            jdbcTemplate.update(MOZO, usuarioId);
        }
        if (roles.contains(Rol.ROLE_ADMIN) && usuarioId != null) {
            jdbcTemplate.update(ADMIN, usuarioId);
        }
    }


    private List<Rol> obtenerRolesPorUsuarioId(int usuarioId) {
        List<Rol> roles = new ArrayList<>();

        if (existeEnTabla("clientes", usuarioId)) {
            roles.add(Rol.ROLE_CLIENTE);
        }
        if (existeEnTabla("mozos", usuarioId)) {
            roles.add(Rol.ROLE_MOZO);
        }
        if (existeEnTabla("administradores", usuarioId)) {
            roles.add(Rol.ROLE_ADMIN);
        }

        return roles;
    }

    private boolean existeEnTabla(String tabla, int usuarioId) {
        String sql = "SELECT EXISTS(SELECT 1 FROM " + tabla + " WHERE usuario_id = ?)";
        Boolean existe = jdbcTemplate.queryForObject(sql, Boolean.class, usuarioId);
        return existe;
    }
    
}
