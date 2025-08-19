package com.russo.roma.repositories.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


import com.russo.roma.model.usuarios.TokenVerificacion;
import com.russo.roma.repositories.interfaces.ITokenVerificacionRepository;

@Repository
public class TokenVerificacionRepository implements ITokenVerificacionRepository{

    private JdbcTemplate jdbcTemplate;

    public TokenVerificacionRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String ALTA = "INSERT INTO verificacion_tokens (token,usuario_id,fecha_expiracion, tipo) VALUES (?,?,?,?)";
    private static final String BUSCAR_USUARIO = "SELECT * FROM verificacion_tokens WHERE usuario_id = ? ORDER BY fecha_expiracion DESC LIMIT 1";
    private static final String BUSCAR_TOKEN = "SELECT * FROM verificacion_tokens WHERE token = ?";
    private static final String MODIFICAR_TOKEN = "UPDATE verificacion_tokens SET usado = 1 WHERE token = ?";

    RowMapper<TokenVerificacion> rowMapper = ((rs,rowNum)-> new TokenVerificacion(
        rs.getInt("id"),
        UUID.fromString(rs.getString("token")),
        rs.getInt("usuario_id"),
        rs.getObject("fecha_expiracion", LocalDateTime.class),
        rs.getString("tipo"),
        rs.getBoolean("usado")
    ));

    @Override
    public Optional<TokenVerificacion> buscarPorUsuarioId(Integer usuarioId){
        TokenVerificacion  tokenVer;
        try {
            tokenVer = jdbcTemplate.queryForObject(BUSCAR_USUARIO, rowMapper, usuarioId);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            tokenVer = null;
        }
        return Optional.ofNullable(tokenVer);
    }

    @Override
    public Optional<TokenVerificacion> buscarPorToken(String token){
        TokenVerificacion tokenVer;
        try {
            tokenVer = jdbcTemplate.queryForObject(BUSCAR_TOKEN,rowMapper,token);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            tokenVer = null;
        }
        return Optional.ofNullable(tokenVer);
    }

    @Override
    public void altaToken(TokenVerificacion t){
        jdbcTemplate.update(ALTA, t.getToken().toString(),t.getUsuarioId(),t.getFechaExpiracion(),t.getTipo());
    }

    public void marcarComoUsado(TokenVerificacion t){
        jdbcTemplate.update(MODIFICAR_TOKEN, t.getToken().toString());
    }
}
