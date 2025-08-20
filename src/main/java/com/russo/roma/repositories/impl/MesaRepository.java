package com.russo.roma.repositories.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.russo.roma.model.negocio.Mesa;
import com.russo.roma.repositories.interfaces.IMesasRepository;

@Repository
public class MesaRepository implements IMesasRepository{

    private static final String ALTA_MESA = "INSERT INTO mesas (numero, ubicacion, activa) VALUES (?,?,?)";

    private static final String BUSCAR_NUMERO = "SELECT * FROM mesas WHERE numero = ?";

    private static final String LISTAR  = "SELECT * FROM mesas";

    private static final String MODIFICAR = "UPDATE mesas SET ubicacion = ?, activa = ? WHERE numero = ?";

    private static final String BORRAR  = "DELETE FROM mesas WHERE numero = ?";

    private final JdbcTemplate jdbcTemplate;

    public MesaRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    RowMapper<Mesa> rowMapperMesa = ((rs,rowNum)-> new Mesa(
        rs.getInt("numero"), 
        rs.getString("ubicacion"), 
        rs.getBoolean("activa")));

    @Override
    public List<Mesa> listar() {
        return jdbcTemplate.query(LISTAR, rowMapperMesa);
    }

    @Override
    public Optional<Mesa> buscarPorNumero(Integer numero) {
        Mesa mesa;
        try {
            mesa = jdbcTemplate.queryForObject(BUSCAR_NUMERO, rowMapperMesa, numero);
        } catch (EmptyResultDataAccessException e) {
            mesa  = null;
        }
        return Optional.ofNullable(mesa);
    }

    @Override
    public void modificar(Mesa mesa) {
        jdbcTemplate.update(MODIFICAR, mesa.getUbicacion(), mesa.getActiva(), mesa.getNumero());
    }

    @Override
    public void borrar(Mesa mesa) {
        jdbcTemplate.update(BORRAR, mesa.getNumero());
    }

    @Override
    public Integer alta(Mesa mesa) {
        jdbcTemplate.update(ALTA_MESA, mesa.getNumero(),mesa.getUbicacion(),mesa.getActiva());
        return mesa.getNumero();
    }

}
