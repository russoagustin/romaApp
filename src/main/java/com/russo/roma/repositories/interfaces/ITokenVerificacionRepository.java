package com.russo.roma.repositories.interfaces;

import java.util.Optional;

import com.russo.roma.model.usuarios.TokenVerificacion;

public interface ITokenVerificacionRepository {

    public Optional<TokenVerificacion> buscarPorUsuarioId(Integer usuarioId);
    public Optional<TokenVerificacion> buscarPorToken(String token);
    public void altaToken(TokenVerificacion t);
}
