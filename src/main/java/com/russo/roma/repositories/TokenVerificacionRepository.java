package com.russo.roma.repositories;

import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.russo.roma.model.usuarios.TokenVerificacion;

@Repository
public class TokenVerificacionRepository {

    private JdbcTemplate jdbcTemplate;

    public TokenVerificacionRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String ALTA = "INSERT INTO verificacion_tokens (token,usuario_id,fecha_expiracion) VALUES (?,?,?)";
    private static final String BUSCAR_USUARIO = "SELECT * FROM verificacion_tokens WHERE usuario_id = ? ORDER BY fecha_expiracion DESC LIMIT 1";


    RowMapper<TokenVerificacion> rowMapper = ((rs,rowNum)-> new TokenVerificacion(
        rs.getInt("id"),
        UUID.fromString(rs.getString("token")),
        rs.getInt("usuario_id"),
        rs.getTimestamp("fecha_expiracion").toLocalDateTime()
    ));

    /**
     * 
     * @param usuarioId
     * @return
     * Buscar el token de verificación por el id del usuario asociado, en caso de haber
     * mas de uno token para el usuario debuelve el último.
     */
    public TokenVerificacion buscarPorUsuarioId(Integer usuarioId){
        return jdbcTemplate.queryForObject(BUSCAR_USUARIO, rowMapper, usuarioId);   
    }

    @Transactional
    public void altaToken(TokenVerificacion t){
        jdbcTemplate.update(ALTA, t.getToken().toString(),t.getUsuarioId(),t.getFechaExpiracion());
    }
}
