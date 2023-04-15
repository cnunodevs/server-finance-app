package com.cnunodevs.serverfinanceapp.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cnunodevs.serverfinanceapp.model.domain.MetricaBalance;
import com.cnunodevs.serverfinanceapp.model.dto.MovimientoDTO;
import com.cnunodevs.serverfinanceapp.model.entity.Movimiento;
import com.cnunodevs.serverfinanceapp.model.entity.Presupuesto;
import com.cnunodevs.serverfinanceapp.model.entity.Usuario;
import com.cnunodevs.serverfinanceapp.model.entity.enums.TipoMovimiento;
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
    public ResponseEntity<MetricaBalance> handleGetMetricasBalance(@PathVariable final UUID idUsuario)
            throws EntityNotFoundException {
        final MetricaBalance metrica = movimientosService.getMetricaBalanceByUsuario(idUsuario);
        return ResponseEntity.status(HttpStatus.OK).body(metrica);
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<Page<MovimientoDTO>> handleGetMovimientosByUsuario(
            @RequestParam(defaultValue = "0") final Integer page,
            @RequestParam(defaultValue = "9") final Integer size,
            @PathVariable final UUID idUsuario) {
        final Pageable paging = PageRequest.of(page, size);
        final Example<Movimiento> example = Example
                .of(Movimiento.builder().usuario(Usuario.builder().id(idUsuario).build()).build());
        final Page<Movimiento> pageMovimientos = movimientosService.getMovimientosPaginateByUsuario(paging, example);
        final Page<MovimientoDTO> pageMovimientosDTO = new PageImpl<MovimientoDTO>(
                pageMovimientos.map(movimientoMapper::pojoToDto).toList());
        return ResponseEntity.status(HttpStatus.OK).body(pageMovimientosDTO);
    }

    @GetMapping("/{idPresupuesto}")
    public ResponseEntity<Page<MovimientoDTO>> handleGetMovimientosByPresupuesto(
            @RequestParam(defaultValue = "0") final Integer page,
            @RequestParam(defaultValue = "9") final Integer size,
            @PathVariable final UUID idPresupuesto) {
        final Pageable paging = PageRequest.of(page, size);
        final Example<Movimiento> example = Example
                .of(Movimiento.builder().presupuesto(Presupuesto.builder().id(idPresupuesto).build()).build());
        final Page<Movimiento> pageMovimientos = movimientosService.getMovimientosPaginateByPortafolio(paging, example);
        final Page<MovimientoDTO> pageMovimientosDTO = new PageImpl<MovimientoDTO>(
                pageMovimientos.map(movimientoMapper::pojoToDto).toList());
        return ResponseEntity.status(HttpStatus.OK).body(pageMovimientosDTO);
    }

    @GetMapping("/{idMovimiento}")
    public ResponseEntity<MovimientoDTO> handleGetMovimientoById(@PathVariable final UUID idMovimiento) {
        if (!movimientosService.movimientoAlreadyExist(idMovimiento)) {
            throw new EntityNotFoundException("Movimiento do not exist. UUID: " + idMovimiento);
        }
        final Movimiento movimiento = movimientosService.getMovimientoById(idMovimiento).get();
        final MovimientoDTO movimientoDTO = movimientoMapper.pojoToDto(movimiento);
        return ResponseEntity.status(HttpStatus.OK).body(movimientoDTO);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> handleCreateMovimiento(
            @RequestBody final MovimientoDTO movimientoDTO, 
            @RequestParam Boolean aplicaDescuentoEspecifico,
            @RequestParam(required = false) UUID idCuentaAhorroEspecifica)
            throws IllegalStateException {
        final Movimiento movimiento = movimientoMapper.dtoToPojo(movimientoDTO);
        if (aplicaDescuentoEspecifico && movimiento.getTipo().equals(TipoMovimiento.INGRESO)){
            movimientosService.createMovimientoDescuentoACuentaEspecifica(movimiento, idCuentaAhorroEspecifica);
        }else {
            movimientosService.createMovimiento(movimiento);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> handleDeleteMovimiento(@RequestBody final MovimientoDTO movimientoDTO)
            throws EntityNotFoundException, IllegalCallerException {
        if (!movimientosService.movimientoAlreadyExist(movimientoDTO.getId())) {
            throw new EntityNotFoundException("Movimiento do not exist. UUID: " + movimientoDTO.getId());
        } else if (movimientoDTO.getContabilizable().equals(true)) {
            throw new IllegalCallerException(
                    "Can not delete this movimiento. UUID: " + movimientoDTO.getId() + " is contabilizable");
        }
        movimientosService.deleteMovimientoById(movimientoDTO.getId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> handleDeleteListOfMovimientosByIds(
            @RequestBody final List<MovimientoDTO> movimientosDTO) throws IllegalCallerException {
        final List<Movimiento> movimientos = movimientosDTO.stream().map(movimientoMapper::dtoToPojo).toList();
        movimientos.stream().forEach(movimiento -> {
            if (movimiento.getContabilizable().equals(true)) {
                throw new IllegalCallerException(
                        "Can not delete this movimiento. UUID: " + movimiento.getId() + " is contabilizable");
            }
        });
        movimientosService.deleteAllMovimientos(movimientos);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @InitBinder
    public void initBinder(final WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

}
