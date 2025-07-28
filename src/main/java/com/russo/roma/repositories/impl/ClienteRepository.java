package com.russo.roma.repositories.impl;

import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.russo.roma.model.usuarios.Cliente;
import com.russo.roma.repositories.interfaces.IGestor;

@Repository
public class ClienteRepository implements IGestor<Cliente, Integer>{

    private static final String INSERTAR = "INSERT INTO clientes (usuario_id) VALUES (?)";

    private static final String MODIFICAR = "UPDATE clientes SET puntos = ? WHERE usuario_id = ?";

    private static final String BUSQUEDA_ID = "SELECT id, nombres, apellidos, email, fecha_nac,contrasena,activo, puntos FROM usuarios LEFT JOIN clientes ON usuarios.id = clientes.usuario_id WHERE id = ?";

    private static final String BORRAR = "DELETE FROM clientes WHERE usuario_id = ?";

    private JdbcTemplate jdbcTemplate;

    public ClienteRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }


    RowMapper<Cliente> rowMapperCliente = ((rs,rowNum)-> new Cliente(
        rs.getInt("id"), 
        rs.getString("nombres"), 
        rs.getString("apellidos"), 
        rs.getString("email"), 
        rs.getString("contrasena"), 
        rs.getDate("fecha_nac").toLocalDate(), 
        rs.getBoolean("activo"), 
        rs.getInt("puntos")
    ));

    @Override
    public Optional<Cliente> buscarPorId(Integer id) {
        
        try {
            Cliente cliente = jdbcTemplate.queryForObject(BUSQUEDA_ID, rowMapperCliente, id);
            return Optional.of(cliente);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
        
    }

    @Override
    public Integer alta(Cliente t) {
        try {
            jdbcTemplate.update(INSERTAR, t.getId());
            return t.getId();   //Si no ocurre error se devuelve el id
        } catch (DataAccessException e) {
            return -1;           // En el caso de error se devuelve -1;
        }
    }

    @Override
    public void borrar(Cliente t) {
        jdbcTemplate.update(BORRAR, t.getId());
    }

    @Override
    public void modificar(Cliente t) {
        jdbcTemplate.update(MODIFICAR, t.getPuntos(),t.getId());
    }


}
