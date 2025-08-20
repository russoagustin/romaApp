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
import com.russo.roma.model.negocio.ItemMenu;
import com.russo.roma.repositories.interfaces.IitemMenuRepository;

@Repository
public class ItemMenuRepository implements IitemMenuRepository{

    private static final String BUSCAR_ID = "SELECT * FROM items_menu WHERE id = ?";
    
    private static final String BUSCAR_NOMBRE = "SELECT * FROM items_menu WHERE nombre LIKE ?";

    private static final String ALTA_ITEM = "INSERT INTO items_menu (nombre,precio,categoria,puntos,ingredientes,descripcion,disponible) VALUES (?,?,?,?,?,?,?)";

    private static final String  MODIFICAR_ITEM = "UPDATE items_menu SET nombre = ?, precio = ?, categoria = ?, puntos = ?, ingredientes = ?, descripcion = ?, disponible = ? WHERE id = ?";
    
    private static final String BORRAR_ITEM = "DELETE FROM items_menu WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;

    public ItemMenuRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }


    RowMapper<ItemMenu> rowMapperItem = ((rs,rowNum)-> new ItemMenu(
        rs.getInt("id"), 
        rs.getString("nombre"), 
        rs.getBigDecimal("precio"), 
        CategoriaItemMenu.valueOf(rs.getString("categoria")), 
        rs.getInt("puntos"), 
        rs.getString("ingredientes"), 
        rs.getString("descripcion"), 
        rs.getBoolean("disponible")));

    @Override
    public Optional<ItemMenu> buscarPorId(Integer id) {
        ItemMenu itemMenu;
        try {
            itemMenu = jdbcTemplate.queryForObject(BUSCAR_ID, rowMapperItem,id);
        } catch (Exception e) {
            itemMenu = null;
        }
        return Optional.ofNullable(itemMenu);
    }

    @Override
    public List<ItemMenu> buscarPorNombre(String nombre) {
        return jdbcTemplate.query(BUSCAR_NOMBRE, rowMapperItem,"%" +nombre + "%");
    }

    @Override
    public Integer alta(ItemMenu itemMenu) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(ALTA_ITEM, new String[] {"id"});
            ps.setString(1, itemMenu.getNombre());
            ps.setBigDecimal(2, itemMenu.getPrecio());
            ps.setString(3, itemMenu.getCategoria().name());
            ps.setInt(4, itemMenu.getPuntos());
            ps.setString(5, itemMenu.getIngredientes());
            ps.setString(6, itemMenu.getDescripcion());
            ps.setBoolean(7, itemMenu.getDisponible());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    @Override
    public void modificar(ItemMenu itemMenu) {
        jdbcTemplate.update(MODIFICAR_ITEM, itemMenu.getNombre(),
            itemMenu.getPrecio(),
            itemMenu.getCategoria().name(),
            itemMenu.getPuntos(),
            itemMenu.getIngredientes(),
            itemMenu.getDescripcion(),
            itemMenu.getDisponible(),
            itemMenu.getId());
    }

    @Override
    public void borrar(ItemMenu itemMenu) {
        jdbcTemplate.update(BORRAR_ITEM, itemMenu.getId());
    }

}
