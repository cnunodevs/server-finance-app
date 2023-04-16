package com.cnunodevs.serverfinanceapp.model.domain;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.cnunodevs.serverfinanceapp.model.entity.Movimiento;
import com.cnunodevs.serverfinanceapp.model.entity.Presupuesto;
import com.cnunodevs.serverfinanceapp.model.entity.enums.PeriodoPresupuesto;
import com.cnunodevs.serverfinanceapp.model.entity.enums.TipoMovimiento;

import lombok.Data;

@Data
public class MetricaPresupuesto {

    private Boolean mostraMetricas;
    private double ingresoNecesarioMinimo;
    private double ingresoDisponible;
    private String temporalidad;
    private Map<String, Long> conceptos;
    private Map<String, Long> relacionTipoMovimiento;

    public MetricaPresupuesto(Set<Movimiento> movimientos) {
        this.mostraMetricas = false;
        if (!movimientos.isEmpty()) {
            mostraMetricas = true;
            this.ingresoDisponible = ingresoDisponible(movimientos);
            this.ingresoNecesarioMinimo = ingresoNecesarioMinimo(movimientos);
            this.temporalidad = temporalidad(movimientos);
            this.conceptos = conceptos(movimientos);
            this.relacionTipoMovimiento = relacionTipoMovimiento(movimientos);
        }
    }

    private Map<String, Long> relacionTipoMovimiento(Set<Movimiento> movimientos) {
        return movimientos.stream().collect(Collectors.groupingBy(m -> m.getTipo().toString(), Collectors.counting()));
    }

    private Map<String, Long> conceptos(Set<Movimiento> movimientos) {
        return movimientos.stream().collect(Collectors.groupingBy(m -> m.getConcepto(), Collectors.counting()));
    }

    private String temporalidad(Set<Movimiento> movimientos) {
        return movimientos.stream().findFirst().orElse(Movimiento.builder().presupuesto(Presupuesto.builder().periodo(PeriodoPresupuesto.NA).build()).build()).getPresupuesto().getPeriodo().toString();
    }

    private double ingresoNecesarioMinimo(Set<Movimiento> movimientos) {
        return movimientos.stream().filter(m -> m.getTipo().equals(TipoMovimiento.EGRESO))
                .mapToDouble(m -> m.getImporte().doubleValue()).sum();
    }

    private double ingresoDisponible(Set<Movimiento> movimientos) {
        return movimientos.stream().filter(m -> m.getTipo().equals(TipoMovimiento.INGRESO))
                .mapToDouble(m -> m.getImporte().doubleValue()).sum();
    }
    
}
