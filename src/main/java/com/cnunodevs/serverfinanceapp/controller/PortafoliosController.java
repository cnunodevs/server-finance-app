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

import com.cnunodevs.serverfinanceapp.model.domain.MetricaPortafolio;
import com.cnunodevs.serverfinanceapp.model.dto.PortafolioDTO;
import com.cnunodevs.serverfinanceapp.model.entity.Inversion;
import com.cnunodevs.serverfinanceapp.model.entity.Portafolio;
import com.cnunodevs.serverfinanceapp.model.entity.Usuario;
import com.cnunodevs.serverfinanceapp.model.mapper.PortafolioMapper;
import com.cnunodevs.serverfinanceapp.service.InversionesService;
import com.cnunodevs.serverfinanceapp.service.PortafoliosService;
import com.cnunodevs.serverfinanceapp.service.UsuariosService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/portafolios")
public class PortafoliosController {

    private final PortafoliosService portafoliosService;
    private final InversionesService inversionesService;
    private final UsuariosService usuariosService;
    private final PortafolioMapper portafolioMapper;

    @GetMapping("/metricas/{idPortafolio}")
    public ResponseEntity<MetricaPortafolio> handleGetMetricaPortafolio(@PathVariable final UUID idPortafolio)
            throws EntityNotFoundException {
        if (!portafoliosService.portafolioAlreadyExist(idPortafolio)) {
            throw new EntityNotFoundException("Portafolio do not exist. UUID: " + idPortafolio);
        }
        final List<Inversion> inversiones = inversionesService.findInversionesByPortafolioId(idPortafolio);
        final MetricaPortafolio metrica = portafoliosService
                .getMetricaPortafolioByInversiones(new HashSet<Inversion>(inversiones));
        return ResponseEntity.status(HttpStatus.OK).body(metrica);
    }

    @GetMapping("/metricas")
    public ResponseEntity<List<MetricaPortafolio>> handleGetMetricasPortafolios(@RequestParam final String username) {
        final Usuario usuario = usuariosService.findByUsername(username).get();
        final List<Portafolio> portafolios = portafoliosService.getPortafoliosByUsuario(usuario);
        final List<MetricaPortafolio> metricas = portafoliosService
                .getMetricasPortafolios(new HashSet<Portafolio>(portafolios));
        return ResponseEntity.status(HttpStatus.OK).body(metricas);
    }

    @GetMapping
    public ResponseEntity<List<PortafolioDTO>> handleGetListPortafolios(@RequestParam final String username) {
        final Usuario usuario = usuariosService.findByUsername(username).get();
        final List<Portafolio> portafolios = portafoliosService.getPortafoliosByUsuario(usuario);
        final List<PortafolioDTO> portafoliosDTO = portafolios.stream().map(portafolioMapper::pojoToDto).toList();
        return ResponseEntity.status(HttpStatus.OK).body(portafoliosDTO);
    }

    @GetMapping
    public ResponseEntity<Page<PortafolioDTO>> handleGetPortafoliosPaginate(
            @RequestParam(defaultValue = "0") final Integer page,
            @RequestParam(defaultValue = "9") final Integer size) {
        final Pageable paging = PageRequest.of(page, size);
        final Page<Portafolio> pagePortafolios = portafoliosService.getPortafoliosPaginate(paging);
        final Page<PortafolioDTO> pagePortafoliosDTO = new PageImpl<PortafolioDTO>(
                pagePortafolios.map(portafolioMapper::pojoToDto).toList());
        return ResponseEntity.status(HttpStatus.OK).body(pagePortafoliosDTO);
    }

    @GetMapping("/{idPortafolio}")
    public ResponseEntity<PortafolioDTO> handleGetPortafolioById(@PathVariable final UUID idPortafolio)
            throws EntityNotFoundException {
        if (!portafoliosService.portafolioAlreadyExist(idPortafolio)) {
            throw new EntityNotFoundException("Portafolio do not exist. UUID: " + idPortafolio);
        }
        final Portafolio portafolio = portafoliosService.getPortafolioById(idPortafolio).get();
        final PortafolioDTO portafolioDTO = portafolioMapper.pojoToDto(portafolio);
        return ResponseEntity.status(HttpStatus.OK).body(portafolioDTO);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> handleCreatePortafolio(@RequestBody final PortafolioDTO portafolioDTO)
            throws IllegalStateException {
        if (portafoliosService.similarAlreadyExist(portafolioDTO.getNombre(), portafolioDTO.getIdUsuario())) {
            throw new IllegalStateException("Similar portafolio already exist");
        }
        final Portafolio portafolio = portafolioMapper.dtoToPojo(portafolioDTO);
        portafoliosService.createPortafolio(portafolio);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<HttpStatus> handleGetUpdatePortafolioById(@RequestBody final PortafolioDTO portafolioDTO)
            throws EntityNotFoundException {
        if (!portafoliosService.portafolioAlreadyExist(portafolioDTO.getId())) {
            throw new EntityNotFoundException("Portafolio do not exist. UUID: " + portafolioDTO.getId());
        }
        final Portafolio portafolio = portafolioMapper.dtoToPojo(portafolioDTO);
        portafoliosService.updatePortafolio(portafolio);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // Missing: hasAnyInversion endpoint
    @GetMapping("/has-any-inversion/{idPortafolio}")
    public ResponseEntity<Boolean> handleHasAnyInversionByPortafolioId(@PathVariable final UUID idPortafolio)
            throws EntityNotFoundException {
        if (!portafoliosService.portafolioAlreadyExist(idPortafolio)) {
            throw new EntityNotFoundException("Portafolio do not exist. UUID: " + idPortafolio);
        }
        final Boolean hasAnyInversion = portafoliosService.hasAnyInversion(idPortafolio);
        return ResponseEntity.status(HttpStatus.OK).body(hasAnyInversion);
    }

    // NOTA: Según logica de negocio el portafolio no se deberia poder eliminar si
    // tiene inversiones
    @DeleteMapping
    public ResponseEntity<HttpStatus> handleDeletePortafolioById(@RequestBody final PortafolioDTO portafolioDTO)
            throws EntityNotFoundException {
        if (!portafoliosService.portafolioAlreadyExist(portafolioDTO.getId())) {
            throw new EntityNotFoundException("Portafolio do not exist. UUID: " + portafolioDTO.getId());
        } else if (portafoliosService.hasAnyInversion(portafolioDTO.getId())) {
            throw new IllegalCallerException(
                    "Can not delete this portafolio. UUID: " + portafolioDTO.getId() + " has inversiones");
        }
        portafoliosService.deletePortafolioById(portafolioDTO.getId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @InitBinder
    public void initBinder(final WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

}
