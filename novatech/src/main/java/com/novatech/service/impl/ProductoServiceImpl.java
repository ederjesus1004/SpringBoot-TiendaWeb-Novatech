package com.novatech.service.impl;

import com.novatech.model.Categoria;
import com.novatech.model.Marca;
import com.novatech.model.Producto;
import com.novatech.repository.ProductoRepository;
import com.novatech.service.ProductoService;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    
    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    @Override
    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public List<Producto> buscarPorCategoria(Categoria categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public List<Producto> buscarPorRangoDePrecio(BigDecimal min, BigDecimal max) {
        return productoRepository.findByPrecioBetween(min, max);
    }

    @Override
    public List<Producto> buscarPorMarca(Marca marca) {
        return productoRepository.findByMarca(marca);
    }
    
    @Override
    public List<Producto> buscarPorNombreMarca(String nombreMarca) {
        return productoRepository.findByMarca_NombreContainingIgnoreCase(nombreMarca);
    }

    @Override
    public List<Producto> listarDestacados() {
        return productoRepository.findByDestacadoTrue();
    }

    @Override
    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }
} 