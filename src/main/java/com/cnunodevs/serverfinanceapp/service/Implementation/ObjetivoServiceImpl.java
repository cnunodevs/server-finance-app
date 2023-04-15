package com.cnunodevs.serverfinanceapp.service.Implementation;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.cnunodevs.serverfinanceapp.model.entity.Objetivo;
import com.cnunodevs.serverfinanceapp.repository.ObjetivoRepository;
import com.cnunodevs.serverfinanceapp.service.ObjetivoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ObjetivoServiceImpl implements ObjetivoService {

    private final ObjetivoRepository objetivoRepository;

    @Override
    public Objetivo saveObjetivo(Objetivo objetivo) {
        return objetivoRepository.save(objetivo);
    }

    @Override
    public Objetivo getObjetivoById(UUID objetivoId) {
        return objetivoRepository.getReferenceById(objetivoId);
    }

    @Override
    public void deleteObjetivoById(UUID objetivoId) {
        objetivoRepository.deleteById(objetivoId);
    }

    @Override
    public void updateObjetivo(Objetivo objetivo) {
        objetivoRepository.save(objetivo);
    }
    
}
