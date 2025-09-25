package com.novatech.service;

import com.novatech.model.Usuario;

import java.util.List;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsuarioService extends UserDetailsService {

    List<Usuario> listarTodos();

    Optional<Usuario> buscarPorId(Long id);

    Optional<Usuario> buscarPorEmail(String email);

    Usuario guardar(Usuario usuario);

    void eliminar(Long id);

    boolean existePorEmail(String email);
}