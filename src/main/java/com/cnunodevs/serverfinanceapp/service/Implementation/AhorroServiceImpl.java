package com.cnunodevs.serverfinanceapp.service.Implementation;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.cnunodevs.serverfinanceapp.model.domain.MetricaAhorro;
import com.cnunodevs.serverfinanceapp.model.domain.MetricaAhorros;
import com.cnunodevs.serverfinanceapp.model.entity.Ahorro;
import com.cnunodevs.serverfinanceapp.model.entity.Usuario;
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
    public Ahorro createBolsilloAhorro(Ahorro ahorro) {
        return ahorroRepository.save(ahorro);
    }

    @Override
    public List<Ahorro> getAllAhorros() {
        return new ArrayList<Ahorro>(ahorroRepository.findAll());
    }

    @Override
    public void updateBolsilloAhorro(Ahorro ahorro) {
        ahorroRepository.save(ahorro);
    }

    @Override
    public void deleteBolsilloAhorro(UUID idAhorro) {
        ahorroRepository.deleteById(idAhorro);
    }

    @Override
    public boolean ahorroExistById(UUID idAhorro) {
        return ahorroRepository.existsById(idAhorro);
    }

    @Override
    public MetricaAhorros getMetricaAhorros(long minMonto, long maxMonto, UUID idUsuario) {
        Example<Ahorro> example = Example.of(Ahorro.builder().usuario(Usuario.builder().id(idUsuario).build()).build());
        List<Ahorro> allAhorros = ahorroRepository.findAll(example);
        List<Ahorro> ahorroFiltred = new ArrayList<>();
        if(allAhorros.isEmpty()) {
            return new MetricaAhorros(allAhorros);
        }
        if (maxMonto == 0) {
            ahorroFiltred = allAhorros;
        } else {
            ahorroFiltred = allAhorros.stream()
                                      .filter(ahorro -> ahorro.getImporte().longValue() >= minMonto
                                                 && ahorro.getImporte().longValue() <= maxMonto)
                                      .toList();
        }
        return new MetricaAhorros(ahorroFiltred);
    }

    @Override
    public Page<Ahorro> getAllAhorrosOfUserPaginated(Pageable pageable, UUID idUser) {
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withIgnoreNullValues().withIgnorePaths("automatico");
        Example<Ahorro> example = Example.of(Ahorro.builder().usuario(Usuario.builder().id(idUser).build()).build(), exampleMatcher);
        return ahorroRepository.findAll(example, pageable);
    }

    @Override
    public List<Ahorro> findAhorrosAutomaticosByUsuarioId(UUID idUsuario) {
        Example<Ahorro> example = Example.of(Ahorro.builder().automatico(true).usuario(Usuario.builder().id(idUsuario).build()).build());
        return ahorroRepository.findAll(example);
    }

    @Override
    public Ahorro findAhorroAutomaticoDefaultByUsuarioId(UUID idUsuario) {
        Example<Ahorro> example = Example.of(Ahorro.builder().usuario(Usuario.builder().id(idUsuario).build()).build());
        return ahorroRepository.findOne(example).get();
    }

    @Override
    public Optional<Ahorro> findAhorroById(UUID idAhorro) {
        return ahorroRepository.findById(idAhorro);
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
    public boolean hasCondition(UUID idAhorro) {
        Ahorro ahorro = ahorroRepository.findById(idAhorro).get();
        return ahorro.getCondicion() != null;
    }

    @Override
    public MetricaAhorro getMetricaAhorro(UUID idAhorro) {
        return new MetricaAhorro(ahorroRepository.getReferenceById(idAhorro));
    }

    @Override
    public boolean ahorroExistByNameAndUser(String name, UUID idUser) {
        Example<Ahorro> example = Example.of(Ahorro.builder()
                                                   .nombre(name)
                                                   .usuario(Usuario.builder().id(idUser).build())
                                                   .build());
        return ahorroRepository.findOne(example).isPresent();
    }

    @Override
    public void unableCondicion(UUID idAhorro) {
        Ahorro ahorro = ahorroRepository.findById(idAhorro).get();
        ahorro.setAutomatico(false);
        ahorro.setCondicion(null);
        ahorroRepository.save(ahorro);
    }

    @Override
    public List<Ahorro> findAhorrosByUsuarioId(UUID idUsuario) {
        Example<Ahorro> example = Example.of(Ahorro.builder().usuario(Usuario.builder().id(idUsuario).build()).build());
        return ahorroRepository.findAll(example);
    }
    
}
