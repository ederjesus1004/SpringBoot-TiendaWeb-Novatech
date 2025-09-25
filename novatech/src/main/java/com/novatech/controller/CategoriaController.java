package com.novatech.controller;

import com.novatech.model.Categoria;
import com.novatech.service.CategoriaService;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public String listarCategorias(Model model) {
        model.addAttribute("categorias", categoriaService.listarTodas());
        return "admin/categorias/lista";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNuevaCategoria(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "admin/categorias/formulario";
    }

    @PostMapping("/guardar")
    public String guardarCategoria(@Valid @ModelAttribute Categoria categoria, 
                                  BindingResult result, 
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/categorias/formulario";
        }
        
        if (categoria.getId() == null && categoriaService.existePorNombre(categoria.getNombre())) {
            result.rejectValue("nombre", "error.categoria", "Ya existe una categoría con este nombre");
            return "admin/categorias/formulario";
        }
        
        categoriaService.guardar(categoria);
        redirectAttributes.addFlashAttribute("mensaje", "Categoría guardada con éxito");
        return "redirect:/admin/categorias";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarCategoria(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        var categoriaOpt = categoriaService.buscarPorId(id);
        if (categoriaOpt.isPresent()) {
            model.addAttribute("categoria", categoriaOpt.get());
            return "admin/categorias/formulario";
        } else {
            redirectAttributes.addFlashAttribute("error", "La categoría no existe");
            return "redirect:/admin/categorias";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        var categoriaOpt = categoriaService.buscarPorId(id);
        if (categoriaOpt.isPresent()) {
            try {
                categoriaService.eliminar(id);
                redirectAttributes.addFlashAttribute("mensaje", "Categoría eliminada con éxito");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "No se puede eliminar la categoría porque tiene productos asociados");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "La categoría no existe");
        }
        return "redirect:/admin/categorias";
    }
} 