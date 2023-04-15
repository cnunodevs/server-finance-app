package com.cnunodevs.serverfinanceapp.service;

import com.cnunodevs.serverfinanceapp.model.domain.MetricaAhorros;
import com.cnunodevs.serverfinanceapp.model.entity.Ahorro;


import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

public interface AhorrosService {
    boolean ahorroExist(UUID ahorroID);
    void createBolsilloAhorro(Ahorro ahorro);
    Set<Ahorro> getAllAhorros();
    Page<Ahorro> getAllAhorrosPaginated(Pageable pageable);
    void updateBolsilloAhorro(Ahorro ahorro);
    void deleteBolsilloAhorro(UUID ahorroID);
    MetricaAhorros getMetricaAhorro(Set<Ahorro> ahorros);
}
