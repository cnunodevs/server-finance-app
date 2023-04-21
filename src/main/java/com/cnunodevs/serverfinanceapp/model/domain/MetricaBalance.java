package com.cnunodevs.serverfinanceapp.model.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.cnunodevs.serverfinanceapp.model.entity.Movimiento;
import com.cnunodevs.serverfinanceapp.model.entity.enums.TipoMovimiento;

import lombok.Data;

@Data
public class MetricaBalance {

    private Double monto;
    private Boolean mostraMetricas;
    private HashMap<String, Double> proporcionPorTipo;
    private HashMap<String, HashMap<String, ImporteConcepto>> detalleImporteConceptoPorTipo;

    public static MetricaBalance getMetricaBalance(Double montoBalance, Set<Movimiento> movimientos) {
        MetricaBalance metricaBalance = new MetricaBalance();
        metricaBalance.setMostraMetricas(false);
        if (!movimientos.isEmpty()) {
            metricaBalance.setMostraMetricas(true);
            metricaBalance.setMonto(montoBalance);
            metricaBalance.setProporcionPorTipo(null);
            metricaBalance
                    .setDetalleImporteConceptoPorTipo(detalleImporteConceptoPorTipoOperation(montoBalance, movimientos));
        }
        return metricaBalance;
    }

    private static HashMap<String, HashMap<String, ImporteConcepto>> detalleImporteConceptoPorTipoOperation(
            Double montoBalance,
            Set<Movimiento> movimientos) {

        HashMap<String, HashMap<String, ImporteConcepto>> detalleImporteConceptoPorTipo = new HashMap<String, HashMap<String, ImporteConcepto>>();
        
        Double totalIngresos = 0.0;
        Double totalEgresos = 0.0;

        totalIngresos = movimientos.stream().filter(m -> m.getTipo().equals(TipoMovimiento.INGRESO)).mapToDouble(m -> m.getImporte().doubleValue()).sum();

        totalEgresos = movimientos.stream().filter(m -> m.getTipo().equals(TipoMovimiento.EGRESO)).mapToDouble(m -> m.getImporte().doubleValue()).sum();

        detalleImporteConceptoPorTipo.put("egresos", importePorConcepto(totalEgresos, movimientos.stream().filter(m -> m.getTipo().equals(TipoMovimiento.EGRESO))));
        detalleImporteConceptoPorTipo.put("ingresos", importePorConcepto(totalIngresos, movimientos.stream().filter(m -> m.getTipo().equals(TipoMovimiento.INGRESO))));

        return detalleImporteConceptoPorTipo;
    }

    private static HashMap<String, ImporteConcepto> importePorConcepto(Double montoTotalTipo,
            Stream<Movimiento> movimientosTipo) {
        Map<String, Double> importePorTipo = movimientosTipo
                .collect(Collectors.toMap(m -> m.getConcepto(), m -> m.getImporte().doubleValue()));
        HashMap<String, ImporteConcepto> metricaConcepto = new HashMap<String, ImporteConcepto>();
        importePorTipo.entrySet()
                .forEach(m -> metricaConcepto.put(m.getKey(), new ImporteConcepto(montoTotalTipo, m.getValue())));
        return metricaConcepto;
    }

    public HashMap<String, Double> proporcionTipoOperation(Double montoBalance, Set<Movimiento> movimientos) {
        HashMap<String, Double> proporcionTipo = new HashMap<String, Double>(movimientos.stream().collect(Collectors
                                                .groupingBy(m -> m.getTipo().toString(), 
                                                                Collectors.summingDouble(m -> m.getImporte().doubleValue()))));
        return proporcionTipo;
    }

}
