package com.uc15.ProjetoFinal.data;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Dados digitados na tela de login. Não é uma entidade JPA: nada vai para o banco. */
@Getter
@Setter
@NoArgsConstructor
public class LoginEntity {

    @NotBlank(message = "Informe o usuário.")
    private String usuario;

    @NotBlank(message = "Informe a senha.")
    private String senha;
}
