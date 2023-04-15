package com.cnunodevs.serverfinanceapp.service;

import java.util.UUID;

import com.cnunodevs.serverfinanceapp.model.entity.Condicion;
import com.cnunodevs.serverfinanceapp.model.entity.Movimiento;

public interface CondicionesService {
    void createCondicion(Condicion condicion);
    void editCondicion(Condicion condicion);
    void unableCondicion(UUID condicionID);
    Movimiento applyCondicionIfExist(Movimiento movimiento);
    Movimiento applyCondicionToSpecificAhorro(Movimiento moviento, UUID ahorroID);
}
