package com.uc15.ProjetoFinal.controller;

import com.uc15.ProjetoFinal.data.BookEntity;
import com.uc15.ProjetoFinal.service.BookCommandService;
import com.uc15.ProjetoFinal.service.BookQueryService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** CRUD de livros: recebe a requisição, delega ao serviço e escolhe a tela. */
@Controller
@RequiredArgsConstructor
public class BookController {

    private static final String VOLTAR_PARA_LISTA = "redirect:/listarLivros";

    private final BookQueryService queryService;
    private final BookCommandService commandService;

    // ---------- READ ----------
    @GetMapping("/listarLivros")
    public String listar(@RequestParam(name = "id", required = false) Integer id, Model model) {
        List<BookEntity> livros;
        if (id == null) {
            livros = queryService.findAll();
        } else {
            Optional<BookEntity> encontrado = queryService.findById(id);
            if (encontrado.isPresent()) {
                livros = List.of(encontrado.get());
            } else {
                livros = List.of();
                model.addAttribute("erro", mensagemNaoEncontrado(id));
            }
        }
        model.addAttribute("livros", livros);
        model.addAttribute("buscaId", id);
        return "read";
    }

    // ---------- CREATE ----------
    @GetMapping("/cadastrarLivro")
    public String formCadastro(Model model) {
        model.addAttribute("livro", new BookEntity());
        return "create";
    }

    @PostMapping("/cadForm")
    public String cadastrar(@Valid @ModelAttribute("livro") BookEntity livro,
            BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "create";
        }
        BookEntity novo = commandService.create(livro);
        redirect.addFlashAttribute("sucesso", "Livro cadastrado com ID " + novo.getId() + ".");
        return VOLTAR_PARA_LISTA;
    }

    // ---------- UPDATE ----------
    @GetMapping("/editarLivro/{id}")
    public String formEdicao(@PathVariable("id") Integer id, Model model, RedirectAttributes redirect) {
        Optional<BookEntity> encontrado = queryService.findById(id);
        if (encontrado.isEmpty()) {
            redirect.addFlashAttribute("erro", mensagemNaoEncontrado(id));
            return VOLTAR_PARA_LISTA;
        }
        model.addAttribute("livro", encontrado.get());
        return "update";
    }

    @PostMapping("/updateForm/{id}")
    public String atualizar(@PathVariable("id") Integer id,
            @Valid @ModelAttribute("livro") BookEntity livro,
            BindingResult result, RedirectAttributes redirect) {
        livro.setId(id); // o ID vem sempre da URL, nunca do corpo do formulário
        if (result.hasErrors()) {
            return "update";
        }
        if (commandService.update(id, livro).isPresent()) {
            redirect.addFlashAttribute("sucesso", "Livro " + id + " atualizado com sucesso.");
        } else {
            redirect.addFlashAttribute("erro", mensagemNaoEncontrado(id));
        }
        return VOLTAR_PARA_LISTA;
    }

    // ---------- DELETE ----------
    @PostMapping("/deletar/{id}")
    public String apagar(@PathVariable("id") Integer id, RedirectAttributes redirect) {
        if (commandService.delete(id)) {
            redirect.addFlashAttribute("sucesso", "Livro " + id + " apagado com sucesso.");
        } else {
            redirect.addFlashAttribute("erro", mensagemNaoEncontrado(id));
        }
        return VOLTAR_PARA_LISTA;
    }

    private String mensagemNaoEncontrado(Integer id) {
        return "Livro com ID " + id + " não encontrado.";
    }
}
