package com.russo.roma.repositories.interfaces;

import com.russo.roma.model.usuarios.TokenVerificacion;

public interface ITokenVerificacionRepository {

    public TokenVerificacion buscarPorUsuarioId(Integer usuarioId);
    public TokenVerificacion buscarPorToken(String token);
    public void altaToken(TokenVerificacion t);
}
