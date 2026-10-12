package com.uc15.ProjetoFinal.service;

import com.uc15.ProjetoFinal.data.BookEntity;
import java.util.List;
import java.util.Optional;

/** Contrato somente de LEITURA de livros. */
public interface BookQueryService {

    List<BookEntity> findAll();

    Optional<BookEntity> findById(Integer id);
}
