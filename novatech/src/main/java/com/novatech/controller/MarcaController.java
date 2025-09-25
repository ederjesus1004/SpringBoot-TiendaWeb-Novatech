package com.novatech.controller;

import com.novatech.model.Marca;
import com.novatech.service.MarcaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/admin/marcas")
public class MarcaController {
    
    private final MarcaService marcaService;
    private static final String UPLOAD_DIR = "src/main/resources/static/img/marcas/";
    
    public MarcaController(MarcaService marcaService) {
        this.marcaService = marcaService;
    }
    
    @GetMapping
    public String listarMarcas(Model model) {
        model.addAttribute("marcas", marcaService.listarMarcas());
        return "admin/marcas/listado";
    }
    
    @GetMapping("/nueva")
    public String mostrarFormularioNueva(Model model) {
        model.addAttribute("marca", new Marca());
        return "admin/marcas/formulario";
    }
    
    @PostMapping("/guardar")
    public String guardarMarca(@Valid @ModelAttribute Marca marca, 
                                BindingResult result,
                                @RequestParam(value = "logoFile", required = false) MultipartFile logoFile,
                                RedirectAttributes flash,
                                Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de Marca");
            model.addAttribute("marca", marca);
            return "admin/marcas/formulario";
        }
        
        try {
            if (logoFile != null && !logoFile.isEmpty()) {
                String uniqueFileName = UUID.randomUUID().toString() + "_" + logoFile.getOriginalFilename();
                Path rootPath = Paths.get(UPLOAD_DIR);
                
                if (!Files.exists(rootPath)) {
                    Files.createDirectories(rootPath);
                }
                
                Path fullPath = rootPath.resolve(uniqueFileName);
                Files.write(fullPath, logoFile.getBytes());
                
                marca.setLogo("/img/marcas/" + uniqueFileName);
            }
            
            marcaService.guardarMarca(marca);
            flash.addFlashAttribute("success", "Marca guardada con éxito");
            
        } catch (IOException e) {
            flash.addFlashAttribute("error", "Error al guardar el logo: " + e.getMessage());
        }
        
        return "redirect:/admin/marcas";
    }
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes flash) {
        var optMarca = marcaService.obtenerMarcaPorId(id);
        
        if (optMarca.isPresent()) {
            model.addAttribute("marca", optMarca.get());
            model.addAttribute("titulo", "Editar Marca");
            return "admin/marcas/formulario";
        }
        
        flash.addFlashAttribute("error", "La marca no existe");
        return "redirect:/admin/marcas";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminarMarca(@PathVariable Long id, RedirectAttributes flash) {
        try {
            marcaService.eliminarMarca(id);
            flash.addFlashAttribute("success", "Marca eliminada con éxito");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al eliminar la marca: " + e.getMessage());
        }
        
        return "redirect:/admin/marcas";
    }
} 