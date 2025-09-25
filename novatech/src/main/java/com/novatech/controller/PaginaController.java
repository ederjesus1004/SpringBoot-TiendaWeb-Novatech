package com.novatech.controller;

import com.novatech.service.CategoriaService;
import com.novatech.service.MarcaService;
import com.novatech.service.ProductoService;
import com.novatech.controller.UsuarioController.UsuarioForm;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.servlet.http.HttpSession;

@Controller
public class PaginaController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final MarcaService marcaService;

    public PaginaController(ProductoService productoService,
            CategoriaService categoriaService,
            MarcaService marcaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.marcaService = marcaService;
    }

    @GetMapping("/")
    public String inicio(Model model, HttpSession session) {
        model.addAttribute("productos", productoService.listarDestacados());
        if (session.getAttribute("usuario") != null) {
            session.setAttribute("usuarioLogueado", session.getAttribute("usuario"));
        }
        return "index";
    }

    @GetMapping("/tienda")
    public String tienda(Model model, HttpSession session) {
        model.addAttribute("productos", productoService.listarTodos());
        model.addAttribute("categorias", categoriaService.listarTodas());
        model.addAttribute("marcas", marcaService.listarMarcas());
        if (session.getAttribute("usuario") != null) {
            session.setAttribute("usuarioLogueado", session.getAttribute("usuario"));
        }
        return "tienda";
    }

    @GetMapping("/producto/{id}")
    public String detalleProducto(@PathVariable Long id, Model model) {
        var productoOpt = productoService.buscarPorId(id);
        if (productoOpt.isPresent()) {
            model.addAttribute("producto", productoOpt.get());
            return "producto-detalle";
        }
        return "redirect:/tienda";
    }

    @GetMapping("/about")
    public String about(HttpSession session) {
        if (session.getAttribute("usuario") != null) {
            session.setAttribute("usuarioLogueado", session.getAttribute("usuario"));
        }
        return "about";
    }

    @GetMapping("/blog")
    public String blog(HttpSession session) {
        if (session.getAttribute("usuario") != null) {
            session.setAttribute("usuarioLogueado", session.getAttribute("usuario"));
        }
        return "blog";
    }

    @GetMapping("/contacto")
    public String contacto(HttpSession session) {
        if (session.getAttribute("usuario") != null) {
            session.setAttribute("usuarioLogueado", session.getAttribute("usuario"));
        }
        return "contacto";
    }
}