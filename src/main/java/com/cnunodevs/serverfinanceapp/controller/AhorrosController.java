package com.cnunodevs.serverfinanceapp.controller;

import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cnunodevs.serverfinanceapp.model.domain.MetricaAhorros;
import com.cnunodevs.serverfinanceapp.model.dto.AhorroDTO;
import com.cnunodevs.serverfinanceapp.model.entity.Ahorro;
import com.cnunodevs.serverfinanceapp.model.entity.Objetivo;
import com.cnunodevs.serverfinanceapp.model.mapper.AhorroMapper;
import com.cnunodevs.serverfinanceapp.service.AhorrosService;
import com.cnunodevs.serverfinanceapp.service.ObjetivoService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/ahorros")
@RequiredArgsConstructor
@Validated
public class AhorrosController {
    
    private final AhorrosService ahorrosService;
    private final ObjetivoService objetivoService;
    private final AhorroMapper ahorroMapper;

    @GetMapping
    public ResponseEntity<Page<AhorroDTO>> getAllAhorrosPaginated(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "9") int size) {
            Pageable pageable = PageRequest.of(page, size);
            Page<Ahorro> pages = ahorrosService.getAllAhorrosPaginated(pageable);
            Page<AhorroDTO> pagesDTO = new PageImpl<>(
                pages.stream().map(ahorroMapper::pojoToDto).toList()
                );
            return ResponseEntity.status(HttpStatus.OK).body(pagesDTO);
    }

    @GetMapping("/metricas")
    public ResponseEntity<MetricaAhorros> getMetricas(@RequestParam(defaultValue = "0") int minMonto, @RequestParam(defaultValue = "0") int maxMonto) {
        MetricaAhorros metricas = ahorrosService.getMetricaAhorro(ahorrosService.getAllAhorros());
        return ResponseEntity.status(HttpStatus.CREATED).body(metricas);
    }

    @PutMapping("/transferencia-hacia-disponible")
    public ResponseEntity<HttpStatus> transferToDisponible(@RequestBody AhorroDTO ahorroDTO, @RequestParam Double importeToTransfer) {
        if(!ahorrosService.ahorroExist(ahorroDTO.getId())) {
            throw new EntityNotFoundException("la cuenta de ahorro a la que hace referencia no existe");
        }
        Ahorro ahorro = ahorroMapper.dtoToPojo(ahorroDTO);
        ahorrosService.transferAhorroToDisponible(ahorro, BigDecimal.valueOf(importeToTransfer));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/transferencia-desde-disponible")
    public ResponseEntity<HttpStatus> transferFromDisponible(@RequestBody AhorroDTO ahorroDTO, @RequestParam Double importeToTransfer) {
        if(!ahorrosService.ahorroExist(ahorroDTO.getId())) {
            throw new EntityNotFoundException("la cuenta de ahorro a la que hace referencia no existe");
        }
        Ahorro ahorro = ahorroMapper.dtoToPojo(ahorroDTO);
        ahorrosService.transferDisponibleToAhorro(ahorro, BigDecimal.valueOf(importeToTransfer));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createAhorro(@RequestBody AhorroDTO ahorroDTO) throws IllegalStateException {
        //mejorar, mala comparacion ya que el dto no viene con id
        if(ahorrosService.ahorroExist(ahorroDTO.getId())) {
            throw new IllegalStateException("Similar bolsillo de ahorro already exist");
        }
        Ahorro ahorro = ahorroMapper.dtoToPojo(ahorroDTO);
        Objetivo objetivo = objetivoService.saveObjetivo(ahorro.getObjetivo());
        ahorro.setObjetivo(objetivo);
        ahorrosService.createBolsilloAhorro(ahorro);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
}
