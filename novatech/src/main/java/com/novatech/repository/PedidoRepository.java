package com.novatech.repository;

import com.novatech.model.Pedido;
import com.novatech.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    List<Pedido> findByUsuario(Usuario usuario);
    
    List<Pedido> findByUsuarioOrderByFechaDesc(Usuario usuario);
    
    List<Pedido> findByEstado(Pedido.EstadoPedido estado);
} 