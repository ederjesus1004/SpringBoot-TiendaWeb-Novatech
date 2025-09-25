package com.novatech.config;

import com.novatech.model.Usuario;
import com.novatech.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UsuarioService usuarioService;

    public CustomLoginSuccessHandler(@Lazy UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        String email = authentication.getName();

        Optional<Usuario> optionalUsuario = usuarioService.buscarPorEmail(email);
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();

            // Guardar el usuario en sesión
            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogueado", usuario);

            // Redirección según el dominio del correo
            if (email.endsWith("@novatech.com")) {
                response.sendRedirect("/admin");
            } else {
                response.sendRedirect("/"); // Página general de la tienda
            }
        } else {
            // Fallback si el usuario no se encuentra (aunque no debería pasar)
            response.sendRedirect("/login?error");
        }
    }
}
