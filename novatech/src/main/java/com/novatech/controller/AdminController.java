package com.novatech.controller;

import com.novatech.service.CategoriaService;
import com.novatech.service.PedidoService;
import com.novatech.service.ProductoService;
import com.novatech.service.UsuarioService;
import com.novatech.service.MarcaService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final UsuarioService usuarioService;
    private final PedidoService pedidoService;
    private final MarcaService marcaService;

    
    public AdminController(ProductoService productoService,
                          CategoriaService categoriaService,
                          UsuarioService usuarioService,
                          PedidoService pedidoService,
                          MarcaService marcaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.usuarioService = usuarioService;
        this.pedidoService = pedidoService;
        this.marcaService = marcaService;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("totalProductos", productoService.listarTodos().size());
        model.addAttribute("totalCategorias", categoriaService.listarTodas().size());
        model.addAttribute("totalUsuarios", usuarioService.listarTodos().size());
        model.addAttribute("totalPedidos", pedidoService.listarTodos().size());
        model.addAttribute("totalMarcas", marcaService.listarMarcas().size());
        
        return "admin/dashboard";
    }
} 