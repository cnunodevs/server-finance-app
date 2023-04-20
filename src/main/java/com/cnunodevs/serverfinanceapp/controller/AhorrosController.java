package com.cnunodevs.serverfinanceapp.controller;

import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cnunodevs.serverfinanceapp.exception.NotDeletableException;
import com.cnunodevs.serverfinanceapp.model.domain.MetricaAhorro;
import com.cnunodevs.serverfinanceapp.model.domain.MetricaAhorros;
import com.cnunodevs.serverfinanceapp.model.dto.AhorroDTO;
import com.cnunodevs.serverfinanceapp.model.entity.Ahorro;
import com.cnunodevs.serverfinanceapp.model.mapper.AhorroMapper;
import com.cnunodevs.serverfinanceapp.service.AhorrosService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/ahorros")
@RequiredArgsConstructor
@Validated
public class AhorrosController {
    
    private final AhorrosService ahorrosService;
    private final AhorroMapper ahorroMapper;

    @GetMapping
    public ResponseEntity<Page<AhorroDTO>> getAllAhorrosPaginated(
        @RequestParam UUID idUser,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "9") int size) {
            Pageable pageable = PageRequest.of(page, size);
            Page<Ahorro> pages = ahorrosService.getAllAhorrosOfUserPaginated(pageable, idUser);
            Page<AhorroDTO> pagesDTO = new PageImpl<>(
                pages.stream().map(ahorroMapper::pojoToDto).toList()
                );
            return ResponseEntity.status(HttpStatus.OK).body(pagesDTO);
    }

    @GetMapping("/{idAhorro}")
    public ResponseEntity<AhorroDTO> getAhorroById(@PathVariable UUID idAhorro) {
        if(!ahorrosService.ahorroExistById(idAhorro)) {
            throw new EntityNotFoundException("la cuenta de ahorro a la que hace referencia no existe");
        }
        AhorroDTO ahorroDTO = ahorroMapper.pojoToDto(ahorrosService.findAhorroById(idAhorro).get());
        return ResponseEntity.status(HttpStatus.OK).body(ahorroDTO);
    }

    @GetMapping("/metricas")
    public ResponseEntity<MetricaAhorros> getMetricas(@RequestParam(defaultValue = "0") long minMonto, @RequestParam(defaultValue = "0") long maxMonto) {
        if((minMonto < 0 || maxMonto < 0) || minMonto > maxMonto) {
            throw new IllegalStateException("mal uso de monto maximo y minimo");
        }
        MetricaAhorros metricas = ahorrosService.getMetricaAhorros(minMonto, maxMonto);
        return ResponseEntity.status(HttpStatus.OK).body(metricas);
    }

    @GetMapping("/metricas/{idAhorro}")
    public ResponseEntity<MetricaAhorro> getMetricaOf(@PathVariable UUID idAhorro) {
        if(!ahorrosService.ahorroExistById(idAhorro)) {
            throw new EntityNotFoundException("la cuenta de ahorro a la que hace referencia no existe");
        }

        MetricaAhorro metricaAhorro = ahorrosService.getMetricaAhorro(idAhorro);
        return ResponseEntity.status(HttpStatus.OK).body(metricaAhorro);

    }

    @GetMapping("/ahorros-automaticos")
    public ResponseEntity<Set<AhorroDTO>> getSetOfAhorrosAutomaticos(@RequestParam UUID idUsuario) {
        Set<Ahorro> ahorros = ahorrosService.findAhorrosAutomaticosByUsuarioId(idUsuario);
        Set<AhorroDTO> ahorrosDTO = Set.copyOf(ahorros.stream()
                                                      .map(ahorroMapper::pojoToDto)
                                                      .filter(ahorro -> ahorro.isAutomatico())
                                                      .toList());
        return ResponseEntity.status(HttpStatus.OK).body(ahorrosDTO);
    }

    @GetMapping("/has-condicion/{idAhorro}")
    public ResponseEntity<Boolean> hasCondicion(@PathVariable UUID idAhorro){
        if(!ahorrosService.ahorroExistById(idAhorro)) {
            throw new EntityNotFoundException("la cuenta de ahorro a la que hace referencia no existe");
        }
        return ResponseEntity.status(HttpStatus.OK).body(ahorrosService.hasCondition(idAhorro));
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createAhorro(@RequestBody AhorroDTO ahorroDTO) throws IllegalStateException {
        if(ahorrosService.ahorroExistByNameAndUser(ahorroDTO.getNombre(), ahorroDTO.getIdUsuario())) {
            throw new IllegalStateException("Similar ahorro already exist");
        }
        Ahorro ahorro = ahorroMapper.dtoToPojo(ahorroDTO);
        ahorrosService.createBolsilloAhorro(ahorro);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    

    @PutMapping("/transferencia-hacia-disponible")
    public ResponseEntity<HttpStatus> transferToDisponible(@RequestBody AhorroDTO ahorroDTO, @RequestParam Double importeToTransfer) {
        if(!ahorrosService.ahorroExistById(ahorroDTO.getId())) {
            throw new EntityNotFoundException("la cuenta de ahorro a la que hace referencia no existe");
        }
        Ahorro ahorro = ahorroMapper.dtoToPojo(ahorroDTO);
        ahorrosService.transferAhorroToDisponible(ahorro, BigDecimal.valueOf(importeToTransfer));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/transferencia-desde-disponible")
    public ResponseEntity<HttpStatus> transferFromDisponible(@RequestBody AhorroDTO ahorroDTO, @RequestParam Double importeToTransfer) {
        if(!ahorrosService.ahorroExistById(ahorroDTO.getId())) {
            throw new EntityNotFoundException("la cuenta de ahorro a la que hace referencia no existe");
        }
        Ahorro ahorro = ahorroMapper.dtoToPojo(ahorroDTO);
        ahorrosService.transferDisponibleToAhorro(ahorro, BigDecimal.valueOf(importeToTransfer));
        return ResponseEntity.status(HttpStatus.OK).build();
    } 

    

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteAhorro(@RequestParam UUID idAhorro) throws NotDeletableException {
        if(!ahorrosService.ahorroExistById(idAhorro)) {
            throw new EntityNotFoundException("Inversion do not exist. UUID: " + idAhorro);
        }
        if(ahorrosService.findAhorroById(idAhorro).get().getImporte().longValue() != 0) {
            throw new NotDeletableException("el ahorro tiene dinero, no se puede borrar");
        }

        ahorrosService.deleteBolsilloAhorro(idAhorro);
        return ResponseEntity.status(HttpStatus.OK).build();
        
    }

    @InitBinder
    public void initBinder(final WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
