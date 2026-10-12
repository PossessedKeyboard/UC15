package com.uc15.ProjetoFinal.livro;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.uc15.ProjetoFinal.controller.BookController;
import com.uc15.ProjetoFinal.service.BookCommandService;
import com.uc15.ProjetoFinal.service.BookQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

/**
 * Exclusão de livro (POST /deletar/{id}) usando Mockito.
 */
@ExtendWith(MockitoExtension.class)
class BookExclusaoMockitoTests {

    @Mock
    private BookQueryService queryService;

    @Mock
    private BookCommandService commandService;

    private BookController controller;
    private RedirectAttributesModelMap redirect;

    @BeforeEach
    void setUp() {
        controller = new BookController(queryService, commandService);
        redirect = new RedirectAttributesModelMap();
    }

    @Test
    void apagarLivroExistente() {
        when(commandService.delete(1)).thenReturn(true);

        String view = controller.apagar(1, redirect);

        assertEquals("redirect:/listarLivros", view);
        assertEquals("Livro 1 apagado com sucesso.", redirect.getFlashAttributes().get("sucesso"));
        verify(commandService).delete(1);
    }
/*
    @Test
    void apagarNaoAfetaOutros() {
        when(commandService.delete(1)).thenReturn(true);

        controller.apagar(1, redirect);

        verify(commandService).delete(1);
        verifyNoMoreInteractions(commandService);   // só o delete(1), mais nada
        verifyNoInteractions(queryService);         // nem consultou a lista
    }

    @Test
    void apagarLivroInexistente() {
        when(commandService.delete(99)).thenReturn(false);

        String view = controller.apagar(99, redirect);

        assertEquals("redirect:/listarLivros", view);
        assertEquals("Livro com ID 99 não encontrado.", redirect.getFlashAttributes().get("erro"));
        assertFalse(redirect.getFlashAttributes().containsKey("sucesso"));
    }

    @Test
    void apagarDuasVezes() {
        // 1ª chamada devolve true, 2ª devolve false
        when(commandService.delete(1)).thenReturn(true, false);

        controller.apagar(1, redirect);
        RedirectAttributesModelMap segundaVez = new RedirectAttributesModelMap();
        controller.apagar(1, segundaVez);

        assertEquals("Livro 1 apagado com sucesso.", redirect.getFlashAttributes().get("sucesso"));
        assertEquals("Livro com ID 1 não encontrado.", segundaVez.getFlashAttributes().get("erro"));
        verify(commandService, times(2)).delete(1);
    }*/
}
