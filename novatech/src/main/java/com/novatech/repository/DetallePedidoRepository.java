package com.novatech.repository;

import com.novatech.model.DetallePedido;
import com.novatech.model.Pedido;
import com.novatech.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
    
    List<DetallePedido> findByPedido(Pedido pedido);
    
    List<DetallePedido> findByProducto(Producto producto);
} 