package com.uc15.ProjetoFinal.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Tabela "livros". Também carrega as regras de validação dos formulários. */
@Data
@Entity
@Table(name = "livros")
@NoArgsConstructor
@AllArgsConstructor
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Informe o título.")
    @Size(max = 255, message = "O título deve ter no máximo 255 caracteres.")
    @Column(nullable = false)
    private String titulo;

    @NotBlank(message = "Informe o autor.")
    @Size(max = 255, message = "O autor deve ter no máximo 255 caracteres.")
    @Column(nullable = false)
    private String autor;

    @NotNull(message = "Informe o ano de lançamento.")
    @Min(value = 1, message = "O ano deve ser maior que zero.")
    @Max(value = 9999, message = "O ano deve ter no máximo 4 dígitos.")
    @Column(nullable = false)
    private Integer ano;

    @NotBlank(message = "Informe a editora.")
    @Size(max = 255, message = "A editora deve ter no máximo 255 caracteres.")
    @Column(nullable = false)
    private String editora;
}
