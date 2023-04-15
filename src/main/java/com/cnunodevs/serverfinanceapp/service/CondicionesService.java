package com.cnunodevs.serverfinanceapp.service;

import java.util.UUID;

import com.cnunodevs.serverfinanceapp.model.entity.Condicion;
import com.cnunodevs.serverfinanceapp.model.entity.Movimiento;

public interface CondicionesService {
    void createCondicion(Condicion condicion);
    void editCondicion(Condicion condicion);
    void unableCondicion(UUID condicionID);
    Movimiento applyConditionIfExist(Movimiento movimiento);
}
