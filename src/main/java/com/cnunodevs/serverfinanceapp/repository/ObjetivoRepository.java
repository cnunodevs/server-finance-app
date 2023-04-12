package com.cnunodevs.serverfinanceapp.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cnunodevs.serverfinanceapp.model.entity.Activo;

public interface ObjetivoRepository extends JpaRepository<Activo, UUID> {
    
}
