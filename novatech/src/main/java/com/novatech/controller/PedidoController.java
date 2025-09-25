package com.novatech.controller;

import com.novatech.model.DetallePedido;
import com.novatech.model.Pedido;
import com.novatech.model.Producto;
import com.novatech.service.PedidoService;
import com.novatech.service.ProductoService;
import com.novatech.service.UsuarioService;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final UsuarioService usuarioService;
    private final ProductoService productoService;
    
    public PedidoController(PedidoService pedidoService, UsuarioService usuarioService, ProductoService productoService) {
        this.pedidoService = pedidoService;
        this.usuarioService = usuarioService;
        this.productoService = productoService;
    }

    @GetMapping
    public String listarPedidos(Model model) {
        model.addAttribute("pedidos", pedidoService.listarTodos());
        model.addAttribute("estados", Pedido.EstadoPedido.values());
        return "admin/pedidos/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoPedido(Model model) {
        PedidoForm pedidoForm = new PedidoForm();
        pedidoForm.setFecha(LocalDateTime.now());
        pedidoForm.setEstado(Pedido.EstadoPedido.PENDIENTE);
        pedidoForm.setDetalles(new ArrayList<>());
        
        model.addAttribute("pedidoForm", pedidoForm);
        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("estados", Pedido.EstadoPedido.values());
        model.addAttribute("productos", productoService.listarTodos());
        return "admin/pedidos/formulario";
    }

    @PostMapping("/guardar")
    public String guardarPedido(@Valid @ModelAttribute PedidoForm pedidoForm,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("usuarios", usuarioService.listarTodos());
            model.addAttribute("estados", Pedido.EstadoPedido.values());
            model.addAttribute("productos", productoService.listarTodos());
            return "admin/pedidos/formulario";
        }
        
        
        if (pedidoForm.getId() != null) {
            redirectAttributes.addFlashAttribute("error", "No se permite editar pedidos existentes");
            return "redirect:/admin/pedidos";
        }
        
       
        Pedido pedido = new Pedido();
        pedido.setFecha(pedidoForm.getFecha());
        pedido.setEstado(pedidoForm.getEstado());
        pedido.setTotal(pedidoForm.getTotal());
        
        var usuarioOpt = usuarioService.buscarPorId(pedidoForm.getUsuarioId());
        if (usuarioOpt.isPresent()) {
            pedido.setUsuario(usuarioOpt.get());
        }
        
        
        Pedido pedidoGuardado = pedidoService.guardar(pedido);
        
       
        if (pedidoForm.getDetalles() != null && !pedidoForm.getDetalles().isEmpty()) {
            BigDecimal totalPedido = BigDecimal.ZERO;
            
            for (DetalleForm detalleForm : pedidoForm.getDetalles()) {
                if (detalleForm.getProductoId() != null && detalleForm.getCantidad() != null && detalleForm.getCantidad() > 0) {
                    var productoOpt = productoService.buscarPorId(detalleForm.getProductoId());
                    if (productoOpt.isPresent()) {
                        Producto producto = productoOpt.get();
                        
                        DetallePedido detalle = new DetallePedido();
                        detalle.setPedido(pedidoGuardado);
                        detalle.setProducto(producto);
                        detalle.setCantidad(detalleForm.getCantidad());
                        detalle.setPrecioUnitario(producto.getPrecio());
                        detalle.calcularSubtotal();
                        
                        pedidoGuardado.getDetalles().add(detalle);
                        totalPedido = totalPedido.add(detalle.getSubtotal());
                    }
                }
            }
            
            
            pedidoGuardado.setTotal(totalPedido);
            pedidoService.guardar(pedidoGuardado);
        }
        
        redirectAttributes.addFlashAttribute("mensaje", "Pedido creado con éxito");
        return "redirect:/admin/pedidos";
    }

    
    @GetMapping("/editar/{id}")
    public String intentarEditarPedido(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "No se permite editar pedidos existentes por seguridad");
        return "redirect:/admin/pedidos/detalles/" + id;
    }
    
    
    @PostMapping("/cambiar-estado/{id}")
    public String cambiarEstadoPedido(@PathVariable Long id, 
                                      @RequestParam("estado") Pedido.EstadoPedido nuevoEstado,
                                      @RequestParam(value = "redirect", defaultValue = "detalles") String redirect,
                                      RedirectAttributes redirectAttributes) {
        var pedidoOpt = pedidoService.buscarPorId(id);
        if (pedidoOpt.isPresent()) {
            Pedido pedido = pedidoOpt.get();
            pedido.setEstado(nuevoEstado);
            pedidoService.guardar(pedido);
            redirectAttributes.addFlashAttribute("mensaje", "Estado del pedido actualizado correctamente");
        } else {
            redirectAttributes.addFlashAttribute("error", "El pedido no existe");
        }
        
        
        if ("lista".equals(redirect)) {
            return "redirect:/admin/pedidos";
        } else {
            return "redirect:/admin/pedidos/detalles/" + id;
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarPedido(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        var pedidoOpt = pedidoService.buscarPorId(id);
        if (pedidoOpt.isPresent()) {
            
            pedidoService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Pedido eliminado con éxito");
        } else {
            redirectAttributes.addFlashAttribute("error", "El pedido no existe");
        }
        return "redirect:/admin/pedidos";
    }

    @GetMapping("/detalles/{id}")
    public String verDetallesPedido(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        var pedidoOpt = pedidoService.buscarPorId(id);
        if (pedidoOpt.isPresent()) {
            model.addAttribute("pedido", pedidoOpt.get());
            model.addAttribute("estados", Pedido.EstadoPedido.values());
            return "admin/pedidos/detalles";
        } else {
            redirectAttributes.addFlashAttribute("error", "El pedido no existe");
            return "redirect:/admin/pedidos";
        }
    }
    
    
    @PostMapping("/agregar-producto")
    public String agregarProducto(@RequestParam("pedidoId") Long pedidoId,
                                  @RequestParam("productoId") Long productoId,
                                  @RequestParam("cantidad") Integer cantidad,
                                  RedirectAttributes redirectAttributes) {
        if (pedidoId != null) {
            
            redirectAttributes.addFlashAttribute("error", "No se permite modificar pedidos existentes");
            return "redirect:/admin/pedidos/detalles/" + pedidoId;
        } else {
            
            redirectAttributes.addFlashAttribute("nuevoProductoId", productoId);
            redirectAttributes.addFlashAttribute("nuevaCantidad", cantidad);
            return "redirect:/admin/pedidos/nuevo";
        }
    }
    
   
    @Data
    public static class PedidoForm {
        private Long id;
        private LocalDateTime fecha;
        private Pedido.EstadoPedido estado;
        private BigDecimal total = BigDecimal.ZERO;
        private Long usuarioId;
        private List<DetalleForm> detalles = new ArrayList<>();
    }
    
    
    @Data
    public static class DetalleForm {
        private Long id;
        private Long productoId;
        private Integer cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;
    }
} 