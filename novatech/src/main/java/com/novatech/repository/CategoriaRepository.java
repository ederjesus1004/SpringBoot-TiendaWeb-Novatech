package com.novatech.repository;

import com.novatech.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    Categoria findByNombre(String nombre);
    
    boolean existsByNombre(String nombre);
} 