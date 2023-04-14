package com.cnunodevs.serverfinanceapp.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cnunodevs.serverfinanceapp.model.entity.Movimiento;

public interface MovimientoRepository extends JpaRepository<Movimiento, UUID> {
    
}
