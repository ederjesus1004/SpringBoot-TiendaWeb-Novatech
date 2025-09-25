package com.novatech.service.impl;

import com.novatech.model.DetallePedido;
import com.novatech.model.Pedido;
import com.novatech.model.Producto;
import com.novatech.repository.DetallePedidoRepository;
import com.novatech.service.DetallePedidoService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetallePedidoServiceImpl implements DetallePedidoService {

    private final DetallePedidoRepository detallePedidoRepository;

    
    public DetallePedidoServiceImpl(DetallePedidoRepository detallePedidoRepository) {
        this.detallePedidoRepository = detallePedidoRepository;
    }

    @Override
    public List<DetallePedido> listarTodos() {
        return detallePedidoRepository.findAll();
    }

    @Override
    public Optional<DetallePedido> buscarPorId(Long id) {
        return detallePedidoRepository.findById(id);
    }

    @Override
    public List<DetallePedido> buscarPorPedido(Pedido pedido) {
        return detallePedidoRepository.findByPedido(pedido);
    }

    @Override
    public List<DetallePedido> buscarPorProducto(Producto producto) {
        return detallePedidoRepository.findByProducto(producto);
    }

    @Override
    public DetallePedido guardar(DetallePedido detallePedido) {
        return detallePedidoRepository.save(detallePedido);
    }

    @Override
    public void eliminar(Long id) {
        detallePedidoRepository.deleteById(id);
    }
} 