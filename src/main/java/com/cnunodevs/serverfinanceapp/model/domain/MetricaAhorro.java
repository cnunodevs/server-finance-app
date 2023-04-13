package com.cnunodevs.serverfinanceapp.model.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MetricaAhorro {
    
    private BigDecimal ahorroTotal;
    private String ahorroAutomatico;
    private BigDecimal ahorroTotal;
    private BigDecimal nivelCumplimientoPromedio;
}
