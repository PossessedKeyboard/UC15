package com.uc15.ProjetoFinal.livro;

import com.uc15.ProjetoFinal.data.BookEntity;
import com.uc15.ProjetoFinal.service.BookCommandService;
import com.uc15.ProjetoFinal.service.BookQueryService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementação "falsa" dos serviços de livro, feita à mão, que guarda os livros
 * em memória. Substitui o banco de dados nos testes, sem usar Mockito.
 */
class FakeBookService implements BookQueryService, BookCommandService {

    private final Map<Integer, BookEntity> livros = new LinkedHashMap<>();
    private int proximoId = 1;
    int chamadasCreate = 0;
    int chamadasUpdate = 0;

    @Override
    public List<BookEntity> findAll() {
        return new ArrayList<>(livros.values());
    }

    @Override
    public Optional<BookEntity> findById(Integer id) {
        return Optional.ofNullable(livros.get(id));
    }

    @Override
    public BookEntity create(BookEntity book) {
        chamadasCreate++;
        book.setId(proximoId++);
        livros.put(book.getId(), book);
        return book;
    }

    @Override
    public Optional<BookEntity> update(Integer id, BookEntity data) {
        chamadasUpdate++;
        if (!livros.containsKey(id)) {
            return Optional.empty();
        }
        data.setId(id);
        livros.put(id, data);
        return Optional.of(data);
    }

    @Override
    public boolean delete(Integer id) {
        return livros.remove(id) != null;
    }
}
