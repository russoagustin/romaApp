package com.russo.roma.repositories.impl;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.russo.roma.model.negocio.CategoriaItemMenu;
import com.russo.roma.model.negocio.EstadoLineaComanda;
import com.russo.roma.model.negocio.ItemMenu;
import com.russo.roma.model.negocio.LineaComanda;
import com.russo.roma.repositories.interfaces.ILineaComandaRepository;

@Repository
public class LineaComandaRepository implements ILineaComandaRepository{

    private final JdbcTemplate jdbcTemplate;

    public LineaComandaRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String BUSQUEDA_ID = "SELECT * FROM lineas_comanda lc INNER JOIN item_menu it ON it.id = lc.item_id WHERE lc.id = ?";

    private static final String BUSQUEDA = "SELECT * FROM lineas_comanda lc INNER JOIN item_menu it ON it.id = lc.item_id WHERE lc.comanda_id = ?";

    private static final String INSERT = "INSERT INTO lineas_comanda (comanda_id,item_id,cantidad,precio,estado) VALUES (?,?,?,?,?)";
 
    private static final String MODIFICAR = "UPDATE lineas_comanda SET estado = ? WHERE id = ?";

    private static final String BORRAR = "DELETE FROM lineas_comanda WHERE id = ?";
    
    private RowMapper<LineaComanda> rowMapperLinea = ((rs,rowNum)-> new LineaComanda(
        rs.getInt("id"),
        rs.getInt("comanda_id"),
        new ItemMenu(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getBigDecimal("precio"),
            CategoriaItemMenu.valueOf(rs.getString("categoria")),
            rs.getInt("puntos"),
            rs.getString("ingredientes"),
            rs.getString("descripcion"),
            rs.getBoolean("disponible")
        ),
        rs.getInt("cantidad"),
        rs.getBigDecimal("precio"),
        EstadoLineaComanda.valueOf(rs.getString("estado"))
    ));

    @Override
    public Optional<LineaComanda> buscarPorId(Integer id) {
        LineaComanda linea;
        try {
            linea = jdbcTemplate.queryForObject(BUSQUEDA_ID, rowMapperLinea, id);    
        } catch (org.springframework.dao.EmptyResultDataAccessException  e) {
            linea = null;
        }
        
        return Optional.ofNullable(linea);
    }

    @Override
    public List<LineaComanda> listarLineasComanda(Integer comandaId) {
        return jdbcTemplate.query(BUSQUEDA, rowMapperLinea, comandaId);
    }

    @Override
    public Integer nuevaLinea(LineaComanda lineaComanda) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT, new String[]{"id"});
            ps.setInt(1, lineaComanda.getComandaId());
            ps.setInt(2,lineaComanda.getItem().getId());
            ps.setInt(3, lineaComanda.getCantidad());
            ps.setBigDecimal(4, lineaComanda.getPrecio());
            ps.setString(5, lineaComanda.getEstado().name());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    @Override
    public void modificarLinea(LineaComanda lineaComanda) {
        jdbcTemplate.update(MODIFICAR, lineaComanda.getEstado(),lineaComanda.getId());
    }

    @Override
    public void borrarLinea(Integer id) {
        jdbcTemplate.update(BORRAR, id);
    }


}
