package com.cnunodevs.serverfinanceapp.model.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CondicionDTO {
    
    private UUID id;

    private String expresion;

    private Double importe;

    private String tipoImporte;

    private Boolean enabled;
}
