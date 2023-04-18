package com.cnunodevs.serverfinanceapp.service;

import java.util.UUID;
import java.util.List;

import com.cnunodevs.serverfinanceapp.model.entity.Objetivo;

public interface ObjetivoService {
    
    Objetivo saveObjetivo(Objetivo objetivo);
    Objetivo getObjetivoById(UUID idObjetivo);
    Boolean objetivoExist(UUID idObjetivo);
    Boolean isObjetivoOfUserDeletable(UUID idUsuario, UUID idObjetivo);
    void deleteObjetivoById(UUID idObjetivo);
    void updateObjetivo(Objetivo objetivo);
    List<Objetivo> findObjetivosBasedOnUserId(UUID id);
    Boolean similarObjetivoExist(String name, UUID idUsuario);
}