package com.cnunodevs.serverfinanceapp.model.dto;

import java.math.BigDecimal;

import com.cnunodevs.serverfinanceapp.model.entity.enums.SectorActivo;
import com.cnunodevs.serverfinanceapp.model.entity.enums.TipoActivo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivosDTO {
    private String nombre;
    private String descripcion;
    private TipoActivo tipo;
    private BigDecimal cantidad;
    private SectorActivo sector;
}
