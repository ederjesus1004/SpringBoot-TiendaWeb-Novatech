package com.novatech.service.impl;

import com.novatech.model.Marca;
import com.novatech.repository.MarcaRepository;
import com.novatech.service.MarcaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MarcaServiceImpl implements MarcaService {
    
    private final MarcaRepository marcaRepository;
    
    public MarcaServiceImpl(MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Marca> listarMarcas() {
        return marcaRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Marca> obtenerMarcaPorId(Long id) {
        return marcaRepository.findById(id);
    }
    
    @Override
    @Transactional
    public Marca guardarMarca(Marca marca) {
        return marcaRepository.save(marca);
    }
    
    @Override
    @Transactional
    public void eliminarMarca(Long id) {
        marcaRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Marca> buscarMarcasPorNombre(String nombre) {
        return marcaRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existeMarcaConNombre(String nombre) {
        return marcaRepository.existsByNombre(nombre);
    }
} 