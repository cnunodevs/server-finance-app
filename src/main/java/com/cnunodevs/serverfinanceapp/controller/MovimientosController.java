package com.cnunodevs.serverfinanceapp.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cnunodevs.serverfinanceapp.model.domain.MetricaBalance;
import com.cnunodevs.serverfinanceapp.model.dto.MovimientoDTO;
import com.cnunodevs.serverfinanceapp.model.entity.Movimiento;
import com.cnunodevs.serverfinanceapp.model.entity.Usuario;
import com.cnunodevs.serverfinanceapp.model.mapper.MovimientoMapper;
import com.cnunodevs.serverfinanceapp.service.MovimientosService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/movimientos")
public class MovimientosController {

    private final MovimientosService movimientosService;
    private final MovimientoMapper movimientoMapper;

    @GetMapping("/metrica/{idUsuario}")
    public ResponseEntity<MetricaBalance> handleGetMetricasBalance(@PathVariable UUID idUsuario) throws EntityNotFoundException {
        MetricaBalance metrica = movimientosService.getMetricaBalanceByUsuario(idUsuario);
        return ResponseEntity.status(HttpStatus.OK).body(metrica);
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<Page<MovimientoDTO>> handleGetMovimientos(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "9") Integer size,
            @PathVariable UUID idUsuario) {
        Pageable paging = PageRequest.of(page, size);
        Example<Movimiento> example = Example
                .of(Movimiento.builder().usuario(Usuario.builder().id(idUsuario).build()).build());
        Page<Movimiento> pageMovimientos = movimientosService.getMovimientosPaginateByUsuario(paging, example);
        Page<MovimientoDTO> pageMovimientosDTO = new PageImpl<MovimientoDTO>(
            pageMovimientos.map(movimientoMapper::pojoToDto).toList());
        return ResponseEntity.status(HttpStatus.OK).body(pageMovimientosDTO);
    }

    @GetMapping("/{idMovimiento}")
    public ResponseEntity<MovimientoDTO> handleGetMovimientoById(@PathVariable UUID idMovimiento) {
        if (!movimientosService.movimientoAlreadyExist(idMovimiento)) {
            throw new EntityNotFoundException("Movimiento do not exist. UUID: " + idMovimiento);
        }
        Movimiento movimiento = movimientosService.getMovimientoById(idMovimiento).get();
        MovimientoDTO movimientoDTO = movimientoMapper.pojoToDto(movimiento);
        return ResponseEntity.status(HttpStatus.OK).body(movimientoDTO);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
    
}
