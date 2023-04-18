package com.cnunodevs.serverfinanceapp.service;

import java.util.UUID;
import java.util.List;

import com.cnunodevs.serverfinanceapp.model.entity.Objetivo;

public interface ObjetivoService {
    
    Objetivo saveObjetivo(Objetivo objetivo);
    Objetivo getObjetivoById(UUID objetivoId);
    Boolean objetivoExist(UUID objetivoId);
    Boolean isObjetivoOfUserDeletable(UUID usuarioId, UUID idObjetivo);
    void deleteObjetivoById(UUID objetivoId);
    void updateObjetivo(Objetivo objetivo);
    List<Objetivo> findObjetivosBasedOnUserId(UUID id);
    Boolean similarObjetivoExist(String name, UUID usuarioId);
}