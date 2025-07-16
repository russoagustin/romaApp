package com.russo.roma.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.russo.roma.model.usuarios.Usuario;

@Repository
public class UsuarioRepository implements IUsuarioRepository{

    //CONSULTAS SQL
    
    //buscar por id
    private String BUSQUEDA_ID = "SELECT * FROM usuarios WHERE id = ?";

    // insertar usuario
    private String INSERTAR = "INSERT INTO usuarios (nombres,apellidos,email,password,fecha_nac, activo) VALUES (?,?,?,?,?,?)";

    // buscar por nonmbre
    private String BUSQUEDA_NOMBRE = "SELECT * FROM usuarios WHERE nombres LIKE ?";

    // borrar usuario
    private String DELETE = "DELETE FROM usuarios WHERE id = ?";

    private String CLIENTES = "SELECT usuario_id FROM clientes WHERE usuario_id = ?";

    private String MOZOS = "SELECT usuario_id FROM mozos WHERE usuario_id = ?";


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
        Usuario usuario = jdbcTemplate.queryForObject(BUSQUEDA_ID, UsuarioRM, id);

        // Obtener roles de las tablas clientes, mozos y administradores
        if (usuario != null){
            List<String> roles = obtenerRolesPorUsuarioId(id);
            usuario.setRoles(roles);
        }

        return Optional.ofNullable(usuario);
    }

    @Override
    public List<Usuario> buscarPorNombre(String nombre) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarPorNombre'");
    }

    @Override
    public void alta(Usuario usuario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'alta'");
    }

    @Override
    public void borrar(Usuario usuario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'borrar'");
    }

    @Override
    public void modificar(Usuario usuario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'modificar'");
    }


    private List<String> obtenerRolesPorUsuarioId(int usuarioId) {
        List<String> roles = new ArrayList<>();

        if (existeEnTabla("clientes", usuarioId)) {
            roles.add("CLIENTE");
        }
        if (existeEnTabla("mozos", usuarioId)) {
            roles.add("MOZO");
        }
        if (existeEnTabla("administradores", usuarioId)) {
            roles.add("ADMIN");
        }

        return roles;
    }

    private boolean existeEnTabla(String tabla, int usuarioId) {
        String sql = "SELECT EXISTS(SELECT 1 FROM " + tabla + " WHERE usuario_id = ?)";
        Boolean existe = jdbcTemplate.queryForObject(sql, Boolean.class, usuarioId);
        return Boolean.TRUE.equals(existe);
    }
    
}
