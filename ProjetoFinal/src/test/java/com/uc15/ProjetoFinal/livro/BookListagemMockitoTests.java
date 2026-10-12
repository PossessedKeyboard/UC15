package com.uc15.ProjetoFinal.livro;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.uc15.ProjetoFinal.controller.BookController;
import com.uc15.ProjetoFinal.data.BookEntity;
import com.uc15.ProjetoFinal.service.BookCommandService;
import com.uc15.ProjetoFinal.service.BookQueryService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;

/**
 * Listagem de livros (GET /listarLivros) usando Mockito.
 * Os serviços são "mocks": objetos de mentira cujo comportamento é
 * programado em cada teste com when(...).thenReturn(...).
 */
@ExtendWith(MockitoExtension.class)   // ativa o Mockito no JUnit 5
class BookListagemMockitoTests {

    @Mock
    private BookQueryService queryService;     // mock da leitura

    @Mock
    private BookCommandService commandService; // mock da escrita (não usado aqui)

    private BookController controller;
    private ExtendedModelMap model;

    private final BookEntity livro1 = new BookEntity(1, "Dom Casmurro", "Machado de Assis", 1899, "Garnier");
    private final BookEntity livro2 = new BookEntity(2, "O Cortiço", "Aluísio Azevedo", 1890, "Garnier");
    private final BookEntity livro3 = new BookEntity(3, "Vidas Secas", "Graciliano Ramos", 1938, "José Olympio");

    @BeforeEach
    void setUp() {
        controller = new BookController(queryService, commandService);
        model = new ExtendedModelMap();
    }

    @SuppressWarnings("unchecked")
    private List<BookEntity> livrosNoModel() {
        return (List<BookEntity>) model.getAttribute("livros");
    }

    @Test
    void listarTodos() {
        when(queryService.findAll()).thenReturn(List.of(livro1, livro2, livro3));

        String view = controller.listar(null, model);

        assertEquals("read", view);
        assertEquals(3, livrosNoModel().size());
        assertNull(model.getAttribute("erro"));
        verify(queryService).findAll();                   // findAll foi chamado
        verify(queryService, never()).findById(any());    // e a busca por ID não
    }

    @Test
    void pesquisarIdExistente() {
        when(queryService.findById(2)).thenReturn(Optional.of(livro2));

        String view = controller.listar(2, model);

        assertEquals("read", view);
        assertEquals(List.of(livro2), livrosNoModel());
        assertEquals(2, model.getAttribute("buscaId"));
        verify(queryService, never()).findAll();
    }

    @Test
    void pesquisarIdInexistente() {
        when(queryService.findById(99)).thenReturn(Optional.empty());

        String view = controller.listar(99, model);

        assertEquals("read", view);
        assertTrue(livrosNoModel().isEmpty());
        assertEquals("Livro com ID 99 não encontrado.", model.getAttribute("erro"));
    }

    @Test
    void listarSemLivros() {
        when(queryService.findAll()).thenReturn(List.of());

        String view = controller.listar(null, model);

        assertEquals("read", view);
        assertTrue(livrosNoModel().isEmpty());
        assertNull(model.getAttribute("erro"));
    }
}
