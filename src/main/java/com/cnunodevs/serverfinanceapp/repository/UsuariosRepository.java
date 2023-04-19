package com.cnunodevs.serverfinanceapp.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cnunodevs.serverfinanceapp.model.entity.Usuario;

public interface UsuariosRepository extends JpaRepository<Usuario, UUID> {

    Optional<Usuario> findFirstByUsername(String username);
    
}
