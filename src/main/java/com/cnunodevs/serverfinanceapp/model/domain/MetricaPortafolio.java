package com.cnunodevs.serverfinanceapp.model.domain;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.cnunodevs.serverfinanceapp.model.entity.Inversion;
import com.cnunodevs.serverfinanceapp.model.entity.enums.PerfilRiesgo;
import com.cnunodevs.serverfinanceapp.model.entity.enums.PlazoInversion;

import lombok.Data;

@Data
public class MetricaPortafolio {

        private Double valorTotal;
        private Double rentabilidadPromedio;
        private Double gananciaEsperada;
        private String plazo;
        private String nivelRiesgo;
        private Map<String, Long> sectores;

        public MetricaPortafolio(Set<Inversion> inversiones) {
                this.valorTotal = inversiones.stream()
                                .mapToDouble(inversion -> inversion.getCantidad() * inversion.getPrecio().doubleValue())
                                .sum();
                this.rentabilidadPromedio = inversiones.stream()
                                .mapToDouble(inversion -> inversion.getRentabilidadEsperada().doubleValue()).sum()
                                / inversiones.size();
                this.nivelRiesgo = PerfilRiesgo
                                .getPromedioRiesgo(inversiones.stream().map(Inversion::getPerfilRiesgo).toList())
                                .toString();
                this.plazo = PlazoInversion.getPromedioPlazo(inversiones.stream().map(Inversion::getPlazo).toList())
                                .toString();
                this.gananciaEsperada = inversiones.stream().mapToDouble(
                                i -> ((i.getCantidad() * i.getPrecio().doubleValue())
                                                * i.getRentabilidadEsperada().doubleValue())
                                                - i.getPrecio().doubleValue())
                                .sum();
                this.sectores = inversiones.stream().collect(Collectors.groupingBy(
                                inversion -> inversion.getPerfilRiesgo().toString(), Collectors.counting()));
        }

}
