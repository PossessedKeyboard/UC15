package com.uc15.ProjetoFinal.livro;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.uc15.ProjetoFinal.controller.BookController;
import com.uc15.ProjetoFinal.data.BookEntity;
import com.uc15.ProjetoFinal.service.BookCommandService;
import com.uc15.ProjetoFinal.service.BookQueryService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

/**
 * Edição de livro usando Mockito:
 *  - GET  /editarLivro/{id}  -> formEdicao()
 *  - POST /updateForm/{id}   -> atualizar()
 */
@ExtendWith(MockitoExtension.class)
class BookEdicaoMockitoTests {

    @Mock
    private BookQueryService queryService;

    @Mock
    private BookCommandService commandService;

    /** Captura o livro que o controller passou para commandService.update(...). */
    @Captor
    private ArgumentCaptor<BookEntity> livroCaptor;

    private BookController controller;
    private ExtendedModelMap model;
    private RedirectAttributesModelMap redirect;
    private LocalValidatorFactoryBean validator;

    @BeforeEach
    void setUp() {
        controller = new BookController(queryService, commandService);
        model = new ExtendedModelMap();
        redirect = new RedirectAttributesModelMap();
        validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
    }

    /** Simula o envio do formulário: converte os textos para BookEntity e valida. */
    private BindingResult enviarFormulario(BookEntity livro, String titulo, String autor,
            String ano, String editora) {
        MutablePropertyValues campos = new MutablePropertyValues();
        campos.add("titulo", titulo);
        campos.add("autor", autor);
        campos.add("ano", ano);
        campos.add("editora", editora);

        WebDataBinder binder = new WebDataBinder(livro, "livro");
        binder.setValidator(validator);
        binder.bind(campos);
        binder.validate();
        return binder.getBindingResult();
    }

    // ---------- ABRIR O FORMULÁRIO ----------

    @Test
    void abrirEdicaoIdExistente() {
        BookEntity livro = new BookEntity(1, "Dom Casmurro", "Machado de Assis", 1899, "Garnier");
        when(queryService.findById(1)).thenReturn(Optional.of(livro));

        String view = controller.formEdicao(1, model, redirect);

        assertEquals("update", view);
        assertSame(livro, model.getAttribute("livro"));
    }

    @Test
    void abrirEdicaoIdInexistente() {
        when(queryService.findById(99)).thenReturn(Optional.empty());

        String view = controller.formEdicao(99, model, redirect);

        assertEquals("redirect:/listarLivros", view);
        assertEquals("Livro com ID 99 não encontrado.", redirect.getFlashAttributes().get("erro"));
        assertFalse(model.containsAttribute("livro"));
    }

    // ---------- SALVAR A EDIÇÃO ----------

    @Test
    void atualizarDadosValidos() {
        BookEntity form = new BookEntity();
        BindingResult result = enviarFormulario(form, "Dom Casmurro (2ª ed.)", "Machado de Assis",
                "1900", "Nova Editora");
        when(commandService.update(eq(1), any(BookEntity.class))).thenReturn(Optional.of(form));

        String view = controller.atualizar(1, form, result, redirect);

        assertEquals("redirect:/listarLivros", view);
        assertEquals("Livro 1 atualizado com sucesso.", redirect.getFlashAttributes().get("sucesso"));

        // confere O QUE foi enviado ao serviço
        verify(commandService).update(eq(1), livroCaptor.capture());
        BookEntity enviado = livroCaptor.getValue();
        assertEquals("Dom Casmurro (2ª ed.)", enviado.getTitulo());
        assertEquals(1900, enviado.getAno());
        assertEquals("Nova Editora", enviado.getEditora());
        verify(commandService, never()).create(any());   // editar não cria livro novo
    }

    @Test
    void atualizarAnoComLetras() {
        BookEntity form = new BookEntity();
        BindingResult result = enviarFormulario(form, "Dom Casmurro", "Machado de Assis",
                "abc", "Garnier");

        String view = controller.atualizar(1, form, result, redirect);

        assertTrue(result.hasFieldErrors("ano"));
        assertEquals("update", view);
        verifyNoInteractions(commandService);   // nenhum método do serviço foi chamado
    }

    @Test
    void atualizarTituloVazio() {
        BookEntity form = new BookEntity();
        BindingResult result = enviarFormulario(form, "", "Machado de Assis", "1899", "Garnier");

        String view = controller.atualizar(1, form, result, redirect);

        assertEquals("Informe o título.", result.getFieldError("titulo").getDefaultMessage());
        assertEquals("update", view);
        verify(commandService, never()).update(anyInt(), any());
    }

    /*@Test
    void idVemDaUrl() {
        BookEntity form = new BookEntity();
        form.setId(50); // ID "forjado" no formulário
        BindingResult result = enviarFormulario(form, "Título novo", "Autor", "2000", "Editora");
        when(commandService.update(eq(1), any(BookEntity.class))).thenReturn(Optional.of(form));

        controller.atualizar(1, form, result, redirect);

        verify(commandService).update(eq(1), livroCaptor.capture());
        assertEquals(1, livroCaptor.getValue().getId());
        verify(commandService, never()).update(eq(50), any());
    }*/

    @Test
    void atualizarIdInexistente() {
        BookEntity form = new BookEntity();
        BindingResult result = enviarFormulario(form, "Qualquer", "Autor", "2000", "Editora");
        when(commandService.update(eq(99), any(BookEntity.class))).thenReturn(Optional.empty());

        String view = controller.atualizar(99, form, result, redirect);

        assertEquals("redirect:/listarLivros", view);
        assertEquals("Livro com ID 99 não encontrado.", redirect.getFlashAttributes().get("erro"));
    }
}
