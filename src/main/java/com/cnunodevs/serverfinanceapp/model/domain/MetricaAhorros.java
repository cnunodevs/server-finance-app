package com.cnunodevs.serverfinanceapp.model.domain;
import java.util.Set;

import com.cnunodevs.serverfinanceapp.model.entity.Ahorro;
import lombok.Data;

@Data
public class MetricaAhorros {
    
    private Boolean mostraMetricas;
    private Double ahorroTotal;
    private String ahorroAutomatico;
    private Double promedioAhorro;
    private Double nivelCumplimientoPromedio;

    public MetricaAhorros(Set<Ahorro> ahorros) {
        this.mostraMetricas = false;
        if (!ahorros.isEmpty()) {
            this.mostraMetricas = true;
            this.ahorroTotal = getAhorroTotalOperation(ahorros);
            this.ahorroAutomatico = getAhorroAutomaticoOperation(ahorros);
            this.promedioAhorro = getPromedioAhorroOperation(ahorros);
            this.nivelCumplimientoPromedio = getNivelCumplimientoPromedioOperation(ahorros);     
        }
    }

    private Double getAhorroTotalOperation(Set<Ahorro> ahorros) {
        return ahorros
                    .stream()
                    .mapToDouble(ahorro -> ahorro.getImporte().doubleValue())
                    .sum();
    }

    private String getAhorroAutomaticoOperation(Set<Ahorro> ahorros) {
        StringBuilder ahorroAutomatico = new StringBuilder();
        ahorroAutomatico.append(String.valueOf(ahorros.stream()
                                                    .filter(ahorro -> ahorro.isAutomatico() == true)
                                                    .count()))
                        .append("/")
                        .append(String.valueOf(ahorros.size()))
                        .append(" son ahorros automaticos");
        return ahorroAutomatico.toString();
    }

    private Double getPromedioAhorroOperation(Set<Ahorro> ahorros) {
        return ahorros.stream()
                    .mapToDouble(ahorro -> ahorro.getImporte().doubleValue())
                    .sum() / ahorros.size();
    }

    private Double getNivelCumplimientoPromedioOperation(Set<Ahorro> ahorros) {
        return ahorros
                .stream()
                .mapToDouble(ahorro -> ahorro.getImporte().doubleValue() / ahorro.getObjetivo().getMonto().doubleValue())
                .sum() / ahorros.size();
    }
}
