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

    private double ingresoNecesarioMinimo;
    private double ingresoDisponible;
    private String temporalidad;
    private Map<String, Long> conceptos;
    private Map<String, Long> relacionTipoMovimiento;

    public MetricaPresupuesto(Set<Movimiento> movimientos) {

        this.ingresoDisponible = movimientos.stream().filter(m -> m.getTipo().equals(TipoMovimiento.INGRESO))
                .mapToDouble(m -> m.getImporte().doubleValue()).sum();
        this.ingresoNecesarioMinimo = movimientos.stream().filter(m -> m.getTipo().equals(TipoMovimiento.EGRESO))
                .mapToDouble(m -> m.getImporte().doubleValue()).sum();
        this.temporalidad = movimientos.stream().findFirst().orElse(Movimiento.builder().presupuesto(Presupuesto.builder().periodo(PeriodoPresupuesto.NA).build()).build()).getPresupuesto().getPeriodo().toString();
        this.conceptos = movimientos.stream().collect(Collectors.groupingBy(m -> m.getConcepto(), Collectors.counting()));
        this.relacionTipoMovimiento = movimientos.stream().collect(Collectors.groupingBy(m -> m.getTipo().toString(), Collectors.counting()));
        
    }

}
