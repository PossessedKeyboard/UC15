
package com.uc15.ProjetoFinal.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/** Barra quem não está logado e manda para a tela de login. */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    public static final String SESSION_USUARIO = "usuario";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession sessao = request.getSession(false);
        if (sessao != null && sessao.getAttribute(SESSION_USUARIO) != null) {
            return true;
        }
        response.sendRedirect(request.getContextPath() + "/");
        return false;
    }
}
