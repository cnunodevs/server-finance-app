package com.cnunodevs.serverfinanceapp.service;

import com.cnunodevs.serverfinanceapp.model.domain.MetricaAhorro;
import com.cnunodevs.serverfinanceapp.model.domain.MetricaAhorros;
import com.cnunodevs.serverfinanceapp.model.entity.Ahorro;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

public interface AhorrosService {
    boolean ahorroExistById(UUID ahorroID);
    boolean ahorroExistByNameAndUser(String name, UUID idUser);
    boolean hasCondition(UUID ahorroID);
    void createBolsilloAhorro(Ahorro ahorro);
    Set<Ahorro> getAllAhorros();
    Page<Ahorro> getAllAhorrosOfUserPaginated(Pageable pageable, UUID idUser);
    void updateBolsilloAhorro(Ahorro ahorro);
    void deleteBolsilloAhorro(UUID ahorroID);
    Set<Ahorro> findAhorrosAutomaticosByUsuarioId(UUID ahorroID);
    Ahorro findAhorroAutomaticoDefaultByUsuarioId(UUID ahorroID);
    MetricaAhorros getMetricaAhorros(long minMonto, long maxMonto);
    MetricaAhorro getMetricaAhorro(UUID idAhorro);
    Optional<Ahorro> findAhorroById(UUID ahorroID);
    void transferAhorroToDisponible(Ahorro ahorro, BigDecimal ImporteToTransfer);
    void transferDisponibleToAhorro(Ahorro ahorro, BigDecimal ImporteToTransfer);
}
