package com.cnunodevs.serverfinanceapp.model.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ObjetivoDTO {
    private UUID id;
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaEstimada;
    private Double monto;
    private UUID usuarioID;
}
