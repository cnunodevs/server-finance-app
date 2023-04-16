package com.cnunodevs.serverfinanceapp.service.Implementation;


import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.cnunodevs.serverfinanceapp.model.domain.MetricaAhorros;
import com.cnunodevs.serverfinanceapp.model.entity.Ahorro;
import com.cnunodevs.serverfinanceapp.repository.AhorroRepository;
import com.cnunodevs.serverfinanceapp.service.AhorrosService;
import com.cnunodevs.serverfinanceapp.service.MovimientosService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AhorroServiceImpl implements AhorrosService {
    
    private final AhorroRepository ahorroRepository;
    private final MovimientosService movimientosService;

    @Override
    public void createBolsilloAhorro(Ahorro ahorro) {
        ahorroRepository.save(ahorro);
    }

    @Override
    public Set<Ahorro> getAllAhorros() {
        return new HashSet<Ahorro>(ahorroRepository.findAll());
    }

    @Override
    public void updateBolsilloAhorro(Ahorro ahorro) {
        ahorroRepository.save(ahorro);
    }

    @Override
    public void deleteBolsilloAhorro(UUID ahorroID) {
        ahorroRepository.deleteById(ahorroID);
    }

    @Override
    public boolean ahorroExist(UUID ahorroID) {
        return ahorroRepository.existsById(ahorroID);
    }

    @Override
    public MetricaAhorros getMetricaAhorro(Set<Ahorro> ahorros) {
        return new MetricaAhorros(ahorros);
    }

    @Override
    public Page<Ahorro> getAllAhorrosPaginated(Pageable pageable) {
        return ahorroRepository.findAll(pageable);
    }

    @Override
    public Set<Ahorro> findAhorrosAutomaticosByUsuarioId(UUID ahorroID) {
        return ahorroRepository.findAhorrosAutomaticosByUsuarioId(ahorroID);
    }

    @Override
    public Ahorro findAhorroAutomaticoDefaultByUsuarioId(UUID ahorroID) {
        return ahorroRepository.findAhorrosAutomaticosByUsuarioId(ahorroID)
                    .stream()
                    .findFirst()
                    .get();
    }

    @Override
    public Optional<Ahorro> findAhorroById(UUID ahorroID) {
        return ahorroRepository.findById(ahorroID);
    }

    @Override
    public void transferAhorroToDisponible(Ahorro ahorro, BigDecimal ImporteToTransfer) {
        ahorro.setImporte(ahorro.getImporte().subtract(ImporteToTransfer));
        ahorroRepository.save(ahorro);
        movimientosService.crearMovimientoHaciaDisponibleDesdeAhorro(ImporteToTransfer, ahorro.getUsuario().getId());
    }

    @Override
    public void transferDisponibleToAhorro(Ahorro ahorro, BigDecimal ImporteToTransfer) {
        ahorro.setImporte(ahorro.getImporte().add(ImporteToTransfer));
        ahorroRepository.save(ahorro);
        movimientosService.crearMovimientoDesdeDisponibleParaAhorrro(ImporteToTransfer, ahorro.getUsuario().getId());
    }

    @Override
    public boolean hasCondition(UUID ahorroID) {
        Ahorro ahorro = ahorroRepository.findById(ahorroID).get();
        return ahorro.getCondicion() != null;
    }
    
}
