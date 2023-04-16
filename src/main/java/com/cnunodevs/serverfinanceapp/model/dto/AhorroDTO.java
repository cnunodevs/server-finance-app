package com.cnunodevs.serverfinanceapp.model.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonPropertyOrder({"id", "tipo", "importe", "automatico", "idObjetivo", "idCondicion"})
public class AhorroDTO {
    
    @NotEmpty
    private UUID id;
    
    @NotEmpty
    private String tipo;

    @NotEmpty
    private Double importe;

    @NotEmpty
    private boolean automatico;

    @NotEmpty
    private UUID idObjetivo;

    private CondicionDTO condicionDTO;

}
