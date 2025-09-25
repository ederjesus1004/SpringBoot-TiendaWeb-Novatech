package com.novatech.repository;

import com.novatech.model.Categoria;
import com.novatech.model.Marca;
import com.novatech.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    List<Producto> findByCategoria(Categoria categoria);
    
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    
    List<Producto> findByPrecioBetween(BigDecimal min, BigDecimal max);
    
    List<Producto> findByMarca(Marca marca);
    
    List<Producto> findByMarca_NombreContainingIgnoreCase(String nombreMarca);
    
    List<Producto> findByDestacadoTrue();
} 