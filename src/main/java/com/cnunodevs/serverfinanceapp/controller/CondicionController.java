package com.cnunodevs.serverfinanceapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cnunodevs.serverfinanceapp.model.dto.CondicionDTO;
import com.cnunodevs.serverfinanceapp.model.entity.Condicion;
import com.cnunodevs.serverfinanceapp.model.mapper.CondicionMapper;
import com.cnunodevs.serverfinanceapp.service.CondicionesService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/condiciones")
@RequiredArgsConstructor
@Validated
public class CondicionController {
    
    private final CondicionMapper condicionMapper;
    private final CondicionesService condicionService;

    @PostMapping
    public ResponseEntity<HttpStatus> createCondicion(@RequestBody CondicionDTO condicionDTO) {
        Condicion condicion = condicionMapper.dtoToPojo(condicionDTO);
        condicionService.save(condicion);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
