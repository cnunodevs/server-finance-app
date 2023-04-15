package com.cnunodevs.serverfinanceapp.service;

import java.util.UUID;

import com.cnunodevs.serverfinanceapp.model.entity.Objetivo;

public interface ObjetivoService {
    
    Objetivo saveObjetivo(Objetivo objetivo);
    Objetivo getObjetivoById(UUID objetivoId);
    void deleteObjetivoById(UUID objetivoId);
    void updateObjetivo(Objetivo objetivo);
}