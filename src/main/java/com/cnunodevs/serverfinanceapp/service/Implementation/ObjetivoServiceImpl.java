package com.cnunodevs.serverfinanceapp.service.Implementation;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.cnunodevs.serverfinanceapp.model.entity.Objetivo;
import com.cnunodevs.serverfinanceapp.model.entity.Usuario;
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

    @Override
    public List<Objetivo> findObjetivosBasedOnUserId(UUID usuarioId) {
        return objetivoRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public Boolean similarObjetivoExist(String name, UUID usuarioId) {
        Example<Objetivo> exampleObjetivos = Example.of(Objetivo.builder()
                                                                .nombre(name)
                                                                .usuario(Usuario.builder()
                                                                                .id(usuarioId)
                                                                                .build())
                                                                .build());
        return objetivoRepository.findOne(exampleObjetivos).isPresent();
    }

    @Override
    public Boolean isObjetivoOfUserDeletable(UUID usuarioId) {
        return objetivoRepository.findObjetivosInAhorrosOfUser(usuarioId).isEmpty() 
                && objetivoRepository.findObjetivosInPortafoslioOfUser(usuarioId).isEmpty();
    }
    
}
