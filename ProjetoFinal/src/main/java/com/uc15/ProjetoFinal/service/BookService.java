package com.uc15.ProjetoFinal.service;

import com.uc15.ProjetoFinal.data.BookEntity;
import com.uc15.ProjetoFinal.data.BookRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Regras de negócio dos livros. Não conhece HTTP nem HTML. */
@Service
@RequiredArgsConstructor
public class BookService implements BookQueryService, BookCommandService {

    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BookEntity> findAll() {
        return bookRepository.findAll(Sort.by("id"));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookEntity> findById(Integer id) {
        return bookRepository.findById(id);
    }

    @Override
    @Transactional
    public BookEntity create(BookEntity book) {
        book.setId(null); // garante a criação de um NOVO registro
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public Optional<BookEntity> update(Integer id, BookEntity data) {
        Optional<BookEntity> existing = bookRepository.findById(id);
        if (existing.isEmpty()) {
            return Optional.empty();
        }
        BookEntity book = existing.get();
        book.setTitulo(data.getTitulo());
        book.setAutor(data.getAutor());
        book.setAno(data.getAno());
        book.setEditora(data.getEditora());
        return Optional.of(bookRepository.save(book));
    }

    @Override
    @Transactional
    public boolean delete(Integer id) {
        if (!bookRepository.existsById(id)) {
            return false;
        }
        bookRepository.deleteById(id);
        return true;
    }
}
