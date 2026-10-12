package com.uc15.ProjetoFinal.service;

import com.uc15.ProjetoFinal.data.BookEntity;
import java.util.Optional;

/** Contrato somente de ESCRITA de livros. */
public interface BookCommandService {

    BookEntity create(BookEntity book);

    /** @return o livro atualizado, ou vazio se o ID não existir */
    Optional<BookEntity> update(Integer id, BookEntity data);

    /** @return true se apagou, false se o ID não existir */
    boolean delete(Integer id);
}
