package com.cnunodevs.serverfinanceapp.model.dto;

import java.util.List;
import java.util.UUID;

import com.cnunodevs.serverfinanceapp.model.entity.Movimiento;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonPropertyOrder({"id" , "nombre", "descripcion", "periodo", "idUsuario", "movimientos"})
public class PresupuestoDTO {

    @JsonProperty("id")
    private UUID id;
    
    @NotEmpty
    @JsonProperty("nombre")
    private String nombre;
    
    @NotEmpty
    @JsonProperty("descripcion")
    private String descripcion;
    
    @NotEmpty
    @JsonProperty("periodo")
    private String periodo;
  
    @NotEmpty
    @JsonProperty("idUsuario")
    private UUID idUsuario;
    
    @JsonProperty("movimientos")
    private List<Movimiento> movimientos;
    
}
