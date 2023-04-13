package com.cnunodevs.serverfinanceapp.controller;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cnunodevs.serverfinanceapp.model.domain.MetricaInversion;
import com.cnunodevs.serverfinanceapp.model.dto.InversionDTO;
import com.cnunodevs.serverfinanceapp.model.entity.Inversion;
import com.cnunodevs.serverfinanceapp.model.entity.Portafolio;
import com.cnunodevs.serverfinanceapp.model.mapper.InversionMapper;
import com.cnunodevs.serverfinanceapp.service.InversionesService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequestMapping("api/v1/inversiones")
@RequiredArgsConstructor
@Validated
public class InversionesController {

    private final InversionesService inversionesService;
    private final InversionMapper inversionMapper;

    @GetMapping("/metricas/{idInversion}")
    public ResponseEntity<?> handleGetMetricasInversion(@PathVariable UUID idInversion) throws EntityNotFoundException {
        if (!inversionesService.inversionAlreadyExist(idInversion)) {
            throw new EntityNotFoundException("Inversion do not exist. UUID: + " + idInversion);
        }
        Inversion inversion = inversionesService.getInversionById(idInversion).get();
        MetricaInversion metrica = inversionesService.getMetricaInversion(inversion);
        return ResponseEntity.status(HttpStatus.OK).body(metrica);
    }

    @GetMapping("/metricas")
    public ResponseEntity<?> handleGetMetricasInversiones(@RequestParam UUID idPortafolio) {
        List<Inversion> inversiones = inversionesService.findInversionesByPortafolioId(idPortafolio);
        List<MetricaInversion> metricas = inversionesService
                .getMetricasInversiones(new HashSet<Inversion>(inversiones));
        return ResponseEntity.status(HttpStatus.OK).body(metricas);
    }

    @GetMapping
    public ResponseEntity<?> handleGetInversiones(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "9") Integer size,
            @RequestParam UUID idPortafolio) {
        Pageable paging = PageRequest.of(page, size);
        Example<Inversion> example = Example
                .of(Inversion.builder().portafolio(Portafolio.builder().id(idPortafolio).build()).build());
        Page<Inversion> pageInversiones = inversionesService.getInversionesPaginateByPortafolio(paging, example);
        Page<InversionDTO> pageInversionesDTO = new PageImpl<InversionDTO>(
                pageInversiones.map(inversionMapper::pojoToDto).toList());
        return ResponseEntity.status(HttpStatus.OK).body(pageInversionesDTO);
    }

    @GetMapping("/{idInversion}")
    public ResponseEntity<?> handleGetInversionById(@PathVariable UUID idInversion) {
        if (!inversionesService.inversionAlreadyExist(idInversion)) {
            throw new EntityNotFoundException("Inversion do not exist. UUID: + " + idInversion);
        }
        Inversion inversion = inversionesService.getInversionById(idInversion).get();
        InversionDTO inversionDTO = inversionMapper.pojoToDto(inversion);
        return ResponseEntity.status(HttpStatus.OK).body(inversionDTO);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> handleUpdateInversion(@RequestBody InversionDTO inversionDTO)
            throws EntityNotFoundException {
        if (!inversionesService.inversionAlreadyExist(inversionDTO.getId())) {
            throw new EntityNotFoundException("Inversion do not exist. UUID: + " + inversionDTO.getId());
        }
        Inversion inversion = inversionMapper.dtoToPojo(inversionDTO);
        inversionesService.updateInversion(inversion);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping
    public ResponseEntity<?> handleDeleteInversionById(@RequestBody InversionDTO inversionDTO)
            throws EntityNotFoundException {
        if (!inversionesService.inversionAlreadyExist(inversionDTO.getId())) {
            throw new EntityNotFoundException("Inversion do not exist. UUID: + " + inversionDTO.getId());
        }
        inversionesService.deleteInversionById(inversionDTO.getId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping
    public ResponseEntity<?> handleDeleteListOfInversionByIds(@RequestBody List<UUID> IdInversiones) {
        inversionesService.deleteAllInversionesById(IdInversiones);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

}
