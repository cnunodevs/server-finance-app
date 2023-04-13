package com.cnunodevs.serverfinanceapp.model.dto;

import java.util.UUID;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AhorroDTO {
    
    private UUID id;
    
    private String tipo;

    private Double importe;

    private boolean automatico;

    private ObjetivoDTO objetivo;

    private CondicionDTO condicion;

}
