package com.cnunodevs.serverfinanceapp.service;

import java.util.UUID;

import com.cnunodevs.serverfinanceapp.model.entity.Condicion;
import com.cnunodevs.serverfinanceapp.model.entity.Movimiento;

public interface CondicionesService {
    void deleteCondicion(UUID idAhorro);
    void save(Condicion condicion);
    Movimiento applyCondicionIfExist(Movimiento movimiento);
    Movimiento applyCondicionToSpecificAhorro(Movimiento moviento, UUID idAhorro);
}
