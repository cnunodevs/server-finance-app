package com.cnunodevs.serverfinanceapp.controller;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cnunodevs.serverfinanceapp.model.domain.MetricaPresupuesto;
import com.cnunodevs.serverfinanceapp.model.dto.PresupuestoDTO;
import com.cnunodevs.serverfinanceapp.model.entity.Movimiento;
import com.cnunodevs.serverfinanceapp.model.entity.Presupuesto;
import com.cnunodevs.serverfinanceapp.model.entity.Usuario;
import com.cnunodevs.serverfinanceapp.model.mapper.PresupuestoMapper;
import com.cnunodevs.serverfinanceapp.service.MovimientosService;
import com.cnunodevs.serverfinanceapp.service.PresupuestosService;
import com.cnunodevs.serverfinanceapp.service.UsuariosService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/presupuestos")
public class PresupuestosController {

    private final PresupuestosService presupuestosService;
    private final MovimientosService movimientosService;
    private final UsuariosService usuariosService;
    private final PresupuestoMapper presupuestoMapper;

    @GetMapping("/metricas/{idPresupuesto}")
    public ResponseEntity<MetricaPresupuesto> handleGetMetricaPresupuesto(@PathVariable UUID idPresupuesto)
            throws EntityNotFoundException {
        if (!presupuestosService.presupuestoAlreadyExist(idPresupuesto)) {
            throw new EntityNotFoundException("Presupuesto do not exist. UUID: " + idPresupuesto);
        }
        List<Movimiento> movimientos = movimientosService.findMovimientosByPresupuestoId(idPresupuesto);
        MetricaPresupuesto metrica = presupuestosService
                .getMetricaPresupuestoByMovimientos(new HashSet<Movimiento>(movimientos));
        return ResponseEntity.status(HttpStatus.OK).body(metrica);
    }

    @GetMapping("/metricas")
    public ResponseEntity<List<MetricaPresupuesto>> handleGetMetricasPresupuestos(@RequestParam String username) {
        Usuario usuario = usuariosService.findByUsername(username).get();
        List<Presupuesto> presupuestos = presupuestosService.getPresupuestosByUsuario(usuario);
        List<MetricaPresupuesto> metricas = presupuestosService
                .getMetricasPresupuestos(new HashSet<Presupuesto>(presupuestos));
        return ResponseEntity.status(HttpStatus.OK).body(metricas);
    }

    @GetMapping
    public ResponseEntity<List<PresupuestoDTO>> handleGetListPresupuestos(@RequestParam String username) {
        Usuario usuario = usuariosService.findByUsername(username).get();
        List<Presupuesto> presupuestos = presupuestosService.getPresupuestosByUsuario(usuario);
        List<PresupuestoDTO> portafoliosDTO = presupuestos.stream().map(presupuestoMapper::pojoToDto).toList();
        return ResponseEntity.status(HttpStatus.OK).body(portafoliosDTO);
    }

    @GetMapping
    public ResponseEntity<Page<PresupuestoDTO>> handleGetPresupuestosPaginate(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "9") Integer size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Presupuesto> pagePresupuestos = presupuestosService.getPresupuestosPaginate(paging);
        Page<PresupuestoDTO> pagePresupuestosDTO = new PageImpl<PresupuestoDTO>(
                pagePresupuestos.map(presupuestoMapper::pojoToDto).toList());
        return ResponseEntity.status(HttpStatus.OK).body(pagePresupuestosDTO);
    }

    @GetMapping("/{idPresupuesto}")
    public ResponseEntity<PresupuestoDTO> handleGetPresupuestoById(@PathVariable UUID idPresupuesto)
            throws EntityNotFoundException {
        if (!presupuestosService.presupuestoAlreadyExist(idPresupuesto)) {
            throw new EntityNotFoundException("Presupuesto do not exist. UUID: " + idPresupuesto);
        }
        Presupuesto presupuesto = presupuestosService.getPresupuestoById(idPresupuesto).get();
        PresupuestoDTO presupuestoDTO = presupuestoMapper.pojoToDto(presupuesto);
        return ResponseEntity.status(HttpStatus.OK).body(presupuestoDTO);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> handleCreatePresupuesto(@RequestBody PresupuestoDTO presupuestoDTO)
            throws IllegalStateException {
        if (presupuestosService.similarAlreadyExist(presupuestoDTO.getNombre(), presupuestoDTO.getIdUsuario())) {
            throw new IllegalStateException("Similar presupuesto already exist");
        }
        Presupuesto presupuesto = presupuestoMapper.dtoToPojo(presupuestoDTO);
        presupuestosService.createPresupuesto(presupuesto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<HttpStatus> handleGetUpdatePresupuestoById(@RequestBody PresupuestoDTO presupuestoDTO)
            throws EntityNotFoundException {
        if (!presupuestosService.presupuestoAlreadyExist(presupuestoDTO.getId())) {
            throw new EntityNotFoundException("Presupuesto do not exist. UUID: " + presupuestoDTO.getId());
        }
        Presupuesto presupuesto = presupuestoMapper.dtoToPojo(presupuestoDTO);
        presupuestosService.updatePresupuesto(presupuesto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> handleDeletePresupuestoById(@RequestBody PresupuestoDTO presupuestoDTO)
            throws EntityNotFoundException {
        if (!presupuestosService.presupuestoAlreadyExist(presupuestoDTO.getId())) {
            throw new EntityNotFoundException("Presupuesto do not exist. UUID: " + presupuestoDTO.getId());
        }
        presupuestosService.deletePresupuestoById(presupuestoDTO.getId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

}
