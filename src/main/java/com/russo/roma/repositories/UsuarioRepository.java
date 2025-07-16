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

    private String CLIENTES = "SELECT user_id FROM clientes WHERE user_id = ?";

    private String MOZOS = "SELECT user_id FROM mozos WHERE user_id = ?";


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
        
        // traer los roles de las tablas clientes y mozos
        if (usuario != null){
            List<String> roles = new ArrayList<>();
            if (jdbcTemplate.queryForObject(CLIENTES, Integer.class, id) != null) {
                roles.add("cliente");
            }
            if (jdbcTemplate.queryForObject(MOZOS, Integer.class, id) != null) {
                roles.add("mozo");
            }
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

}
