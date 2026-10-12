package com.uc15.ProjetoFinal.service;

import org.springframework.stereotype.Service;

/**
 * Implementação de TESTE: um único usuário fixo, sem banco de dados.
 * Para algo real, basta criar outra implementação de AuthenticationService.
 */
@Service
public class LoginService implements AuthenticationService {

    private static final String USUARIO_TESTE = "adm";
    private static final String SENHA_TESTE = "123";

    @Override
    public boolean authenticate(String usuario, String senha) {
        return USUARIO_TESTE.equals(usuario) && SENHA_TESTE.equals(senha);
    }
}
