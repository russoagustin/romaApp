package com.russo.roma.repositories;

import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.russo.roma.model.usuarios.Administrador;

@Repository
public class AdministradorRepository implements IGestor<Administrador,Integer>{

    private static final String INSERTAR = "INSERT INTO administradores (usuario_id) VALUES (?)";    

    private static final String BUSQUEDA_ID = "SELECT id, nombres, apellidos, email, fecha_nac,contrasena,activo FROM usuarios LEFT JOIN administradores ON usuarios.id = administradores.usuario_id WHERE id = ?";

    private static final String BORRAR = "DELETE FROM administradores WHERE usuario_id = ?";

    private JdbcTemplate jdbcTemplate;

    public AdministradorRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }


    RowMapper<Administrador> rowMapperAdmin = ((rs,rowNum) -> new Administrador(
            rs.getInt("id"), 
            rs.getString("nombres"), 
            rs.getString("apellidos"), 
            rs.getString("email"), 
            rs.getString("contrasena"), 
            rs.getDate("fecha_nac").toLocalDate(), 
            rs.getBoolean("activo")
        ));

    @Override
    public Optional<Administrador> buscarPorId(Integer id) {
        try {
            Administrador admin = jdbcTemplate.queryForObject(BUSQUEDA_ID, rowMapperAdmin, id);
            return Optional.of(admin);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Integer alta(Administrador t) {
        try {
            jdbcTemplate.update(INSERTAR, t.getId());
            return t.getId();
        } catch (Exception e) {
            return -1;
        }
        
    }

    @Override
    public void borrar(Administrador t) {
        jdbcTemplate.update(BORRAR, t.getId());
    }

    @Override
    @Deprecated
    public void modificar(Administrador t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'modificar'");
    }

}
