package com.novatech.service;

import com.novatech.model.Categoria;
import com.novatech.model.Marca;
import com.novatech.model.Producto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductoService {
    
    List<Producto> listarTodos();
    
    Optional<Producto> buscarPorId(Long id);
    
    List<Producto> buscarPorCategoria(Categoria categoria);
    
    List<Producto> buscarPorNombre(String nombre);
    
    List<Producto> buscarPorRangoDePrecio(BigDecimal min, BigDecimal max);
    
    List<Producto> buscarPorMarca(Marca marca);
    
    List<Producto> buscarPorNombreMarca(String nombreMarca);
    
    List<Producto> listarDestacados();
    
    Producto guardar(Producto producto);
    
    void eliminar(Long id);
} 