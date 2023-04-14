package com.cnunodevs.serverfinanceapp.model.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonPropertyOrder({"id" , "importe", "tipo", "concepto", "idPresupuesto","logoConcepto"})
public class MovimientoDTO {

    @JsonProperty("id")
    private UUID id;

    @NotEmpty
    @JsonProperty("importe")
    private Double importe;

    @NotEmpty
    @JsonProperty("tipo")
    private String tipo;

    @NotEmpty
    @JsonProperty("concepto")
    private String concepto;

    @JsonProperty("idPresupuesto")
    private UUID idPresupuesto;

    @NotEmpty
    @JsonProperty("logoConcepto")
    private String logoConcepto;
    
}
