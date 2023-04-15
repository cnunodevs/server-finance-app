package com.cnunodevs.serverfinanceapp.service.Implementation;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cnunodevs.serverfinanceapp.model.domain.MetricaInversion;
import com.cnunodevs.serverfinanceapp.model.entity.Inversion;
import com.cnunodevs.serverfinanceapp.model.entity.Portafolio;
import com.cnunodevs.serverfinanceapp.repository.InversionesRepository;
import com.cnunodevs.serverfinanceapp.service.InversionesService;
import com.cnunodevs.serverfinanceapp.service.MovimientosService;
import com.cnunodevs.serverfinanceapp.service.PortafoliosService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Primary
@Transactional
@RequiredArgsConstructor
public class InversionesServiceImpl implements InversionesService {

    private final InversionesRepository inversionesRepository;
    private final MovimientosService movimientosService;
    private final PortafoliosService portafoliosService;

    @Override
    public List<Inversion> findInversionesByPortafolioId(UUID idPortafolio) {
        Example<Inversion> example = Example
                .of(Inversion.builder().portafolio(Portafolio.builder().id(idPortafolio).build()).build());
        return inversionesRepository.findAll(example);
    }

    @Override
    public boolean inversionAlreadyExist(UUID idInversion) {
        return inversionesRepository.existsById(idInversion);
    }

    @Override
    public Optional<Inversion> getInversionById(UUID idInversion) {
        return inversionesRepository.findById(idInversion);
    }

    @Override
    public MetricaInversion getMetricaInversion(Inversion inversion) {
        return new MetricaInversion(inversion);
    }

    @Override
    public void deleteInversionById(UUID id) {
        inversionesRepository.deleteById(id);
    }

    @Override
    public void deleteAllInversionesById(List<UUID> idInversiones) {
        inversionesRepository.deleteAllById(idInversiones);
    }

    @Override
    public List<MetricaInversion> getMetricasInversiones(Set<Inversion> inversiones) {
        return inversiones.stream().map(i -> new MetricaInversion(i)).toList();
    }

    @Override
    public Page<Inversion> getInversionesPaginateByPortafolio(Pageable paging, Example<Inversion> example) {
        return inversionesRepository.findAll(example, paging);
    }

    @Override
    public void updateInversion(Inversion inversion) {
        inversionesRepository.save(inversion);
    }

    @Override
    public Boolean alreadyExistOnPortafolio(UUID idPortafolio, String nombre) {
        Example<Inversion> example = Example
                .of(Inversion.builder().nombre(nombre).portafolio(Portafolio.builder().id(idPortafolio).build()).build());
        return inversionesRepository.findOne(example).isPresent();
    }

    @Override
    public void createInversion(Inversion inversion) {
        inversionesRepository.save(inversion);
    }

    @Override
    public void liquidarInversion(Inversion inversion) {
        //Logica para liquidar inversion
        //Si es simulada no se registra cambio en el disponible
        if (inversion.getSimulada().equals(true)){
            deleteInversionById(inversion.getId());
        }
        double valorTotalInversion = inversion.getPrecio().doubleValue() * inversion.getCantidad();
        double gananciaTotal = MetricaInversion.calcularGananciaEsperada(inversion.getRentabilidadEsperada().doubleValue(), valorTotalInversion);
        UUID idUsuario = portafoliosService.getPortafolioById(inversion.getPortafolio().getId()).get().getUsuario().getId();
        movimientosService.crearMovimientoHaciaDisponible(BigDecimal.valueOf(gananciaTotal), idUsuario, "inversion", "logo_inversion");
        deleteInversionById(inversion.getId());

    }

    @Override
    public Boolean portafolioHasAnyInversion(UUID idPortafolio) {
        Example<Inversion> example = Example
                .of(Inversion.builder().portafolio(Portafolio.builder().id(idPortafolio).build()).build());
        return inversionesRepository.findOne(example).isPresent();
    }

}
