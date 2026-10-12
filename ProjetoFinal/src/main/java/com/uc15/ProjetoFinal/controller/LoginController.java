package com.uc15.ProjetoFinal.controller;

import com.uc15.ProjetoFinal.config.LoginInterceptor;
import com.uc15.ProjetoFinal.data.LoginEntity;
import com.uc15.ProjetoFinal.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationService authenticationService;

    @GetMapping("/")
    public String login(HttpServletRequest request, Model model) {
        HttpSession sessao = request.getSession(false);
        if (sessao != null && sessao.getAttribute(LoginInterceptor.SESSION_USUARIO) != null) {
            return "redirect:/menu";
        }
        model.addAttribute("loginForm", new LoginEntity());
        return "login";
    }

    @PostMapping("/loginForm")
    public String authentication(@Valid @ModelAttribute("loginForm") LoginEntity user,
            BindingResult result, HttpServletRequest request, Model model) {
        if (result.hasErrors()) {
            return "login";
        }
        if (!authenticationService.authenticate(user.getUsuario(), user.getSenha())) {
            model.addAttribute("erro", "Usuário ou senha inválidos.");
            return "login";
        }
        HttpSession sessao = request.getSession();
        sessao.setAttribute(LoginInterceptor.SESSION_USUARIO, user.getUsuario());
        return "redirect:/menu";
    }

    @PostMapping("/logoff")
    public String logOff(HttpServletRequest request) {
        HttpSession sessao = request.getSession(false);
        if (sessao != null) {
            sessao.invalidate();
        }
        return "redirect:/";
    }
}
