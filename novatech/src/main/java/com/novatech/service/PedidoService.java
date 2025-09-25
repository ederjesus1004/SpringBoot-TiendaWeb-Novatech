package com.novatech.service;

import com.novatech.model.Pedido;
import com.novatech.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface PedidoService {
    
    List<Pedido> listarTodos();
    
    Optional<Pedido> buscarPorId(Long id);
    
    List<Pedido> buscarPorUsuario(Usuario usuario);
    
    List<Pedido> buscarPorUsuarioOrdenadosPorFecha(Usuario usuario);
    
    List<Pedido> buscarPorEstado(Pedido.EstadoPedido estado);
    
    Pedido guardar(Pedido pedido);
    
    void eliminar(Long id);
} 