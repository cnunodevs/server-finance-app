package com.cnunodevs.serverfinanceapp.service;

import java.util.Optional;

import com.cnunodevs.serverfinanceapp.model.entity.Usuario;

public interface UsuariosService {

    Optional<Usuario> findByUsername(String username);
    
}
