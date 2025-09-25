package com.novatech.service.impl;

import com.novatech.model.Pedido;
import com.novatech.model.Usuario;
import com.novatech.repository.PedidoRepository;
import com.novatech.service.PedidoService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;

    
    public PedidoServiceImpl(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    @Override
    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    @Override
    public List<Pedido> buscarPorUsuario(Usuario usuario) {
        return pedidoRepository.findByUsuario(usuario);
    }

    @Override
    public List<Pedido> buscarPorUsuarioOrdenadosPorFecha(Usuario usuario) {
        return pedidoRepository.findByUsuarioOrderByFechaDesc(usuario);
    }

    @Override
    public List<Pedido> buscarPorEstado(Pedido.EstadoPedido estado) {
        return pedidoRepository.findByEstado(estado);
    }

    @Override
    public Pedido guardar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Override
    public void eliminar(Long id) {
        pedidoRepository.deleteById(id);
    }
} 