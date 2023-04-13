package com.cnunodevs.serverfinanceapp.model.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.cnunodevs.serverfinanceapp.model.entity.Condicion;
import com.cnunodevs.serverfinanceapp.model.entity.Objetivo;
import com.cnunodevs.serverfinanceapp.model.entity.enums.TipoAhorro;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AhorroDTO {
    
    private UUID id;
    
    private String tipo;

    private Double importe;

    private boolean automatico;

    private List<ObjetivoDTO> objetivos;

    private CondicionDTO condicion;

}
