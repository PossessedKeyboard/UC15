package com.uc15.ProjetoFinal.service;

/** Contrato de verificação de credenciais. */
public interface AuthenticationService {

    boolean authenticate(String usuario, String senha);
}
