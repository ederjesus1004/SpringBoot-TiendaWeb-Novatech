package com.novatech.service;

import com.novatech.model.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaService {
    
    List<Categoria> listarTodas();
    
    Optional<Categoria> buscarPorId(Long id);
    
    Categoria buscarPorNombre(String nombre);
    
    Categoria guardar(Categoria categoria);
    
    void eliminar(Long id);
    
    boolean existePorNombre(String nombre);
} 