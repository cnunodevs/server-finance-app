package com.cnunodevs.serverfinanceapp.model.domain;

import java.util.UUID;

import com.cnunodevs.serverfinanceapp.model.entity.Inversion;

import lombok.Data;

@Data
public class MetricaInversion {

    private UUID idInversion;
    private Double gananciaEsperada;
    private Double valorTotal;
    private String plazo;
    private String nivelRiesgo;

    public MetricaInversion(Inversion inversion) {

        this.idInversion = inversion.getId();
        this.valorTotal = inversion.getCantidad() * inversion.getPrecio().doubleValue();
        this.gananciaEsperada = (inversion.getCantidad() * inversion.getPrecio().doubleValue())
                * inversion.getRentabilidadEsperada().doubleValue() - inversion.getPrecio().doubleValue();
        this.plazo = inversion.getPlazo().toString();
        this.nivelRiesgo = inversion.getPerfilRiesgo().toString();

    }

}
