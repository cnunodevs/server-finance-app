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

        this.idInversion = idInversion(inversion);
        this.valorTotal = valorTotal(inversion);
        this.gananciaEsperada = gananciaEsperada(inversion);
        this.plazo = plazo(inversion);
        this.nivelRiesgo = nivelRiesgo(inversion);

    }

    private double gananciaEsperada(Inversion inversion) {
        return valorTotal(inversion)
                * inversion.getRentabilidadEsperada().doubleValue() - inversion.getPrecio().doubleValue();
    }

    private String nivelRiesgo(Inversion inversion) {
        return inversion.getPerfilRiesgo().toString();
    }

    private String plazo(Inversion inversion) {
        return inversion.getPlazo().toString();
    }

    private double valorTotal(Inversion inversion) {
        return inversion.getCantidad() * inversion.getPrecio().doubleValue();
    }

    private UUID idInversion(Inversion inversion) {
        return inversion.getId();
    }

}
