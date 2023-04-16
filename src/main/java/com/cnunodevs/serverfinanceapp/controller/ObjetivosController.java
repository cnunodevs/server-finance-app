package com.cnunodevs.serverfinanceapp.controller;

import java.util.UUID;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cnunodevs.serverfinanceapp.model.dto.ObjetivoDTO;
import com.cnunodevs.serverfinanceapp.model.entity.Objetivo;
import com.cnunodevs.serverfinanceapp.model.mapper.ObjetivoMapper;
import com.cnunodevs.serverfinanceapp.service.ObjetivoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/objetivos")
@RequiredArgsConstructor
@Validated
public class ObjetivosController {
    
    private final ObjetivoService objetivoService;
    private final ObjetivoMapper objetivoMapper;

    @GetMapping("/has-objetivo/{idUsuario}")
    public ResponseEntity<Boolean> userHasObjetivo(@PathVariable UUID idUsuario) {
        List<Objetivo> objetivos = objetivoService.findObjetivosBasedOnUserId(idUsuario);
        return ResponseEntity.status(HttpStatus.OK).body(objetivos.isEmpty());
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createObjetivo(@RequestBody ObjetivoDTO objetivoDTO) {
        if(objetivoService.similarObjetivoExist(objetivoDTO.getNombre(), objetivoDTO.getUsuarioID())) {
            throw new IllegalStateException("Similar objetivo already exist");
        }
        Objetivo objetivo = objetivoMapper.dtoToPojo(objetivoDTO);
        objetivoService.saveObjetivo(objetivo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<Page<ObjetivoDTO>> getAllObjetivosOfUser(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "7") int size,
                                                                @RequestParam UUID usuarioId) {
        Pageable paging = PageRequest.of(page, size);
        List<Objetivo> objetivos = objetivoService.findObjetivosBasedOnUserId(usuarioId);
        Page<ObjetivoDTO> objetivosDTO = new PageImpl<ObjetivoDTO>(objetivos.stream()
                                                                            .map(objetivoMapper::pojoToDto)
                                                                            .toList(),
                                                                   paging,
                                                                   objetivos.size());
        return ResponseEntity.status(HttpStatus.OK).body(objetivosDTO);
    }

    @GetMapping
    public ResponseEntity<List<ObjetivoDTO>> getListObjetivosOfUser(@RequestParam UUID idUsuario) {
        return ResponseEntity.status(HttpStatus.OK).body(objetivoService.findObjetivosBasedOnUserId(idUsuario)
                                                            .stream()
                                                            .map(objetivoMapper::pojoToDto)
                                                            .toList());
    }
}
