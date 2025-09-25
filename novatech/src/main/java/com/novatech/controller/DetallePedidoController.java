package com.novatech.controller;

import com.novatech.model.DetallePedido;
import com.novatech.service.DetallePedidoService;
import com.novatech.service.PedidoService;
import com.novatech.service.ProductoService;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/pedidos/{pedidoId}/detalles")
public class DetallePedidoController {

    private final DetallePedidoService detallePedidoService;
    private final PedidoService pedidoService;
    private final ProductoService productoService;

    
    public DetallePedidoController(DetallePedidoService detallePedidoService, 
                                  PedidoService pedidoService, 
                                  ProductoService productoService) {
        this.detallePedidoService = detallePedidoService;
        this.pedidoService = pedidoService;
        this.productoService = productoService;
    }

    @GetMapping
    public String listarDetallesPedido(@PathVariable Long pedidoId, Model model, RedirectAttributes redirectAttributes) {
        var pedidoOpt = pedidoService.buscarPorId(pedidoId);
        if (pedidoOpt.isPresent()) {
            model.addAttribute("pedido", pedidoOpt.get());
            model.addAttribute("detalles", detallePedidoService.buscarPorPedido(pedidoOpt.get()));
            return "admin/detalles/lista";
        } else {
            redirectAttributes.addFlashAttribute("error", "El pedido no existe");
            return "redirect:/admin/pedidos";
        }
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoDetalle(@PathVariable Long pedidoId, Model model, RedirectAttributes redirectAttributes) {
        var pedidoOpt = pedidoService.buscarPorId(pedidoId);
        if (pedidoOpt.isPresent()) {
            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedidoOpt.get());
            
            model.addAttribute("detalle", detalle);
            model.addAttribute("pedido", pedidoOpt.get());
            model.addAttribute("productos", productoService.listarTodos());
            return "admin/detalles/formulario";
        } else {
            redirectAttributes.addFlashAttribute("error", "El pedido no existe");
            return "redirect:/admin/pedidos";
        }
    }

    @PostMapping("/guardar")
    public String guardarDetalle(@PathVariable Long pedidoId,
                               @Valid @ModelAttribute DetallePedido detalle,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        var pedidoOpt = pedidoService.buscarPorId(pedidoId);
        if (!pedidoOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "El pedido no existe");
            return "redirect:/admin/pedidos";
        }
        
        if (result.hasErrors()) {
            model.addAttribute("pedido", pedidoOpt.get());
            model.addAttribute("productos", productoService.listarTodos());
            return "admin/detalles/formulario";
        }
        
        detalle.setPedido(pedidoOpt.get());
        detallePedidoService.guardar(detalle);
        redirectAttributes.addFlashAttribute("mensaje", "Detalle guardado con éxito");
        return "redirect:/admin/pedidos/" + pedidoId + "/detalles";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarDetalle(@PathVariable Long pedidoId, 
                                               @PathVariable Long id, 
                                               Model model, 
                                               RedirectAttributes redirectAttributes) {
        var pedidoOpt = pedidoService.buscarPorId(pedidoId);
        if (!pedidoOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "El pedido no existe");
            return "redirect:/admin/pedidos";
        }
        
        var detalleOpt = detallePedidoService.buscarPorId(id);
        if (detalleOpt.isPresent()) {
            model.addAttribute("detalle", detalleOpt.get());
            model.addAttribute("pedido", pedidoOpt.get());
            model.addAttribute("productos", productoService.listarTodos());
            return "admin/detalles/formulario";
        } else {
            redirectAttributes.addFlashAttribute("error", "El detalle no existe");
            return "redirect:/admin/pedidos/" + pedidoId + "/detalles";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarDetalle(@PathVariable Long pedidoId, 
                                @PathVariable Long id, 
                                RedirectAttributes redirectAttributes) {
        var pedidoOpt = pedidoService.buscarPorId(pedidoId);
        if (!pedidoOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "El pedido no existe");
            return "redirect:/admin/pedidos";
        }
        
        var detalleOpt = detallePedidoService.buscarPorId(id);
        if (detalleOpt.isPresent()) {
            detallePedidoService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Detalle eliminado con éxito");
        } else {
            redirectAttributes.addFlashAttribute("error", "El detalle no existe");
        }
        return "redirect:/admin/pedidos/" + pedidoId + "/detalles";
    }
} 