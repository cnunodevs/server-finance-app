package com.cnunodevs.serverfinanceapp.service;

import java.util.UUID;
import java.util.List;

import com.cnunodevs.serverfinanceapp.model.entity.Objetivo;

public interface ObjetivoService {
    
    Objetivo saveObjetivo(Objetivo objetivo);
    Objetivo getObjetivoById(UUID objetivoId);
    void deleteObjetivoById(UUID objetivoId);
    void updateObjetivo(Objetivo objetivo);
    List<Objetivo> findObjetivosBasedOnUserId(UUID id);
    Boolean similarObjetivoExist(String name, UUID usuarioId);
}