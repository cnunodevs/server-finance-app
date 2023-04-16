package com.cnunodevs.serverfinanceapp.repository;

import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cnunodevs.serverfinanceapp.model.entity.Objetivo;

public interface ObjetivoRepository extends JpaRepository<Objetivo, UUID> {
    
    @Query(value = "select * from objetivos where usuario_fk = :usuarioId")
    List<Objetivo> findByUsuarioId(UUID usuarioId);
}
