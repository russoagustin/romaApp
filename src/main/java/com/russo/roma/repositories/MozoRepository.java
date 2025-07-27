package com.russo.roma.repositories;

import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.russo.roma.model.usuarios.Mozo;

@Repository
public class MozoRepository implements IGestor<Mozo,Integer>{

    private static final String INSERTAR = "INSERT INTO mozos (usuario_id) VALUES (?)";

    private static final String BUSQUEDA_ID = "SELECT id, nombres, apellidos, email, fecha_nac,contrasena,activo FROM usuarios LEFT JOIN mozos ON usuarios.id = mozos.usuario_id WHERE id = ?";

    private static final String BORRAR = "DELETE FROM mozos WHERE usuario_id = ?";

    private JdbcTemplate jdbcTemplate;

    public MozoRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }


    RowMapper<Mozo> rowMapperMozo = ((rs,rowNum) -> new Mozo(
        rs.getInt("id"), 
        rs.getString("nombres"), 
        rs.getString("apellidos"), 
        rs.getString("email"), 
        rs.getString("contrasena"), 
        rs.getDate("fecha_nac").toLocalDate(), 
        rs.getBoolean("activos")
    ));

    @Override
    public Optional<Mozo> buscarPorId(Integer id) {
        try {
            Mozo mozo = jdbcTemplate.queryForObject(BUSQUEDA_ID, rowMapperMozo, id);
            return Optional.of(mozo);
        } catch (Exception e) {
            return Optional.empty();
        }
        
    }

    @Override
    public Integer alta(Mozo t) {
        try {
            jdbcTemplate.update(INSERTAR, t.getId());
            return t.getId();
        } catch (DataAccessException e) {
            return -1;
        }
    }

    @Override
    public void borrar(Mozo t) {
        jdbcTemplate.update(BORRAR, t.getId());
    }

    @Override
    @Deprecated
    public void modificar(Mozo t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'modificar'");
    }

}
