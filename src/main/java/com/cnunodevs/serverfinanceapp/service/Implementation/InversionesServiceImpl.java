package com.cnunodevs.serverfinanceapp.service.Implementation;

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

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Primary
@Transactional
@RequiredArgsConstructor
public class InversionesServiceImpl implements InversionesService {

    private final InversionesRepository inversionesRepository;

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

}
