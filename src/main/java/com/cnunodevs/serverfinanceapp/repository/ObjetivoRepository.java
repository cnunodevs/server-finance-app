package com.cnunodevs.serverfinanceapp.repository;

import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cnunodevs.serverfinanceapp.model.entity.Objetivo;

public interface ObjetivoRepository extends JpaRepository<Objetivo, UUID> {
    
    @Query(value = "select * from objetivos o where o.usuario_fk = :usuarioId", nativeQuery = true)
    List<Objetivo> findByUsuarioId(UUID usuarioId);

    @Query(value = "select * from objetivos o inner join ahorros a on o.id = a.objetivo_fk where o.usuario_fk = :usuarioId", nativeQuery = true)
    List<Objetivo> findObjetivosInAhorrosOfUser(UUID usuarioId);

    @Query(value = "select * from objetivos o inner join portafiolio p on o.id = p.objetivo_fk where o.usuario_fk = :usuarioId", nativeQuery = true)
    List<Objetivo> findObjetivosInPortafoslioOfUser(UUID usuarioId);
}
