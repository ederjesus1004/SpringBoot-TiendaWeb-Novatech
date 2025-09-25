package com.novatech.controller;

import com.novatech.model.Usuario;
import com.novatech.service.UsuarioService;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.Data;

@Controller
@RequestMapping("/admin/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "admin/usuarios/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoUsuario(Model model) {
        model.addAttribute("usuarioForm", new UsuarioForm());
        return "admin/usuarios/formulario";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@Valid @ModelAttribute("usuarioForm") UsuarioForm usuarioForm,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (usuarioForm.getId() == null && (usuarioForm.getPassword() == null || usuarioForm.getPassword().isEmpty())) {
            result.rejectValue("password", "error.password", "La contraseña es obligatoria para nuevos usuarios");
        }

        if (usuarioForm.getPassword() != null && !usuarioForm.getPassword().isEmpty()
                && usuarioForm.getPassword().length() < 6) {
            result.rejectValue("password", "error.password", "La contraseña debe tener al menos 6 caracteres");
        }

        if (usuarioForm.getId() == null && usuarioService.existePorEmail(usuarioForm.getEmail())) {
            result.rejectValue("email", "error.usuario", "Ya existe un usuario con este email");
        }

        if (result.hasErrors()) {
            return "admin/usuarios/formulario";
        }

        Usuario usuario;
        if (usuarioForm.getId() != null) {

            var usuarioExistente = usuarioService.buscarPorId(usuarioForm.getId());
            if (usuarioExistente.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
                return "redirect:/admin/usuarios";
            }

            usuario = usuarioExistente.get();
            usuario.setNombre(usuarioForm.getNombre());
            usuario.setApellido(usuarioForm.getApellido());
            usuario.setEmail(usuarioForm.getEmail());
            usuario.setTelefono(usuarioForm.getTelefono());
            usuario.setDireccion(usuarioForm.getDireccion());

            if (usuarioForm.getPassword() != null && !usuarioForm.getPassword().isEmpty()) {
                usuario.setPassword(usuarioForm.getPassword());
            }
        } else {

            usuario = new Usuario();
            usuario.setNombre(usuarioForm.getNombre());
            usuario.setApellido(usuarioForm.getApellido());
            usuario.setEmail(usuarioForm.getEmail());
            usuario.setPassword(usuarioForm.getPassword());
            usuario.setTelefono(usuarioForm.getTelefono());
            usuario.setDireccion(usuarioForm.getDireccion());
        }

        usuarioService.guardar(usuario);
        redirectAttributes.addFlashAttribute("mensaje", "Usuario guardado con éxito");
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarUsuario(@PathVariable Long id, Model model,
            RedirectAttributes redirectAttributes) {
        var usuarioOpt = usuarioService.buscarPorId(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            UsuarioForm usuarioForm = new UsuarioForm();
            usuarioForm.setId(usuario.getId());
            usuarioForm.setNombre(usuario.getNombre());
            usuarioForm.setApellido(usuario.getApellido());
            usuarioForm.setEmail(usuario.getEmail());
            usuarioForm.setTelefono(usuario.getTelefono());
            usuarioForm.setDireccion(usuario.getDireccion());

            model.addAttribute("usuarioForm", usuarioForm);
            return "admin/usuarios/formulario";
        } else {
            redirectAttributes.addFlashAttribute("error", "El usuario no existe");
            return "redirect:/admin/usuarios";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        var usuarioOpt = usuarioService.buscarPorId(id);
        if (usuarioOpt.isPresent()) {
            try {
                usuarioService.eliminar(id);
                redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado con éxito");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error",
                        "No se puede eliminar el usuario porque tiene pedidos asociados");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "El usuario no existe");
        }
        return "redirect:/admin/usuarios";
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

    // Registro desde login (público)
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuarioForm", new UsuarioForm());
        return "registro";
    }
}