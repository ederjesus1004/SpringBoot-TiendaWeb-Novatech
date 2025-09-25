package com.novatech.service;

import com.novatech.model.DetallePedido;
import com.novatech.model.Pedido;
import com.novatech.model.Producto;

import java.util.List;
import java.util.Optional;

public interface DetallePedidoService {
    
    List<DetallePedido> listarTodos();
    
    Optional<DetallePedido> buscarPorId(Long id);
    
    List<DetallePedido> buscarPorPedido(Pedido pedido);
    
    List<DetallePedido> buscarPorProducto(Producto producto);
    
    DetallePedido guardar(DetallePedido detallePedido);
    
    void eliminar(Long id);
} 