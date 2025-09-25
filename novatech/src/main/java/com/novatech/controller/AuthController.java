package com.novatech.controller;

import com.novatech.model.Usuario;
import com.novatech.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuarioForm", new UsuarioForm());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@Valid @ModelAttribute("usuarioForm") UsuarioForm usuarioForm,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {
        if (usuarioForm.getPassword() == null || usuarioForm.getPassword().isEmpty()) {
            result.rejectValue("password", "error.password", "La contraseña es obligatoria");
        }
        if (usuarioForm.getPassword() != null && !usuarioForm.getPassword().isEmpty()
                && usuarioForm.getPassword().length() < 6) {
            result.rejectValue("password", "error.password", "La contraseña debe tener al menos 6 caracteres");
        }
        if (usuarioService.existePorEmail(usuarioForm.getEmail())) {
            result.rejectValue("email", "error.usuario", "Ya existe un usuario con este email");
        }
        if (result.hasErrors()) {
            model.addAttribute("usuarioForm", usuarioForm);
            return "registro";
        }
        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioForm.getNombre());
        usuario.setApellido(usuarioForm.getApellido());
        usuario.setEmail(usuarioForm.getEmail());
        usuario.setPassword(usuarioForm.getPassword());
        usuario.setTelefono(usuarioForm.getTelefono());
        usuario.setDireccion(usuarioForm.getDireccion());
        usuarioService.guardar(usuario);
        redirectAttributes.addFlashAttribute("mensaje", "Usuario registrado con éxito. Ahora puedes iniciar sesión.");
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(Model model,
            @RequestParam(value = "error", required = false) String error,
            Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
            if (isAdmin) {
                return "redirect:/admin"; // Admin redirige al dashboard
            } else {
                return "redirect:/"; // Usuario normal redirige a inicio
            }
        }

        model.addAttribute("usuarioForm", new UsuarioForm());
        model.addAttribute("activeTab", "login");
        if (error != null) {
            model.addAttribute("error", "Credenciales inválidas");
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @Data
    public static class UsuarioForm {
        private Long id;
        private String nombre;
        private String apellido;
        private String email;
        private String password;
        private String telefono;
        private String direccion;
    }
}
