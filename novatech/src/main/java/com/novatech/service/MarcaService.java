package com.novatech.service;

import com.novatech.model.Marca;

import java.util.List;
import java.util.Optional;

public interface MarcaService {
    
    List<Marca> listarMarcas();
    
    Optional<Marca> obtenerMarcaPorId(Long id);
    
    Marca guardarMarca(Marca marca);
    
    void eliminarMarca(Long id);
    
    List<Marca> buscarMarcasPorNombre(String nombre);
    
    boolean existeMarcaConNombre(String nombre);
} 