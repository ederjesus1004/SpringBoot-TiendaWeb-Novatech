package com.novatech.controller;

import com.novatech.model.Producto;
import com.novatech.service.CategoriaService;
import com.novatech.service.MarcaService;
import com.novatech.service.ProductoService;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final MarcaService marcaService;

    
    public ProductoController(ProductoService productoService, CategoriaService categoriaService, MarcaService marcaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.marcaService = marcaService;
    }

    @GetMapping
    public String listarProductos(Model model) {
        model.addAttribute("productos", productoService.listarTodos());
        return "admin/productos/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoProducto(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.listarTodas());
        model.addAttribute("marcas", marcaService.listarMarcas());
        return "admin/productos/formulario";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@Valid @ModelAttribute Producto producto,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaService.listarTodas());
            model.addAttribute("marcas", marcaService.listarMarcas());
            return "admin/productos/formulario";
        }
        
        productoService.guardar(producto);
        redirectAttributes.addFlashAttribute("mensaje", "Producto guardado con éxito");
        return "redirect:/admin/productos";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarProducto(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        var productoOpt = productoService.buscarPorId(id);
        if (productoOpt.isPresent()) {
            model.addAttribute("producto", productoOpt.get());
            model.addAttribute("categorias", categoriaService.listarTodas());
            model.addAttribute("marcas", marcaService.listarMarcas());
            return "admin/productos/formulario";
        } else {
            redirectAttributes.addFlashAttribute("error", "El producto no existe");
            return "redirect:/admin/productos";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        var productoOpt = productoService.buscarPorId(id);
        if (productoOpt.isPresent()) {
            try {
                productoService.eliminar(id);
                redirectAttributes.addFlashAttribute("mensaje", "Producto eliminado con éxito");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "No se puede eliminar el producto porque tiene pedidos asociados");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "El producto no existe");
        }
        return "redirect:/admin/productos";
    }
} 