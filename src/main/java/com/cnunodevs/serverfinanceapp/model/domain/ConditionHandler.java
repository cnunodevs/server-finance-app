package com.cnunodevs.serverfinanceapp.model.domain;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.cnunodevs.serverfinanceapp.model.entity.Ahorro;
import com.cnunodevs.serverfinanceapp.model.entity.Condicion;
import com.cnunodevs.serverfinanceapp.model.entity.Movimiento;
import com.cnunodevs.serverfinanceapp.model.entity.enums.Expresion;
import com.cnunodevs.serverfinanceapp.model.entity.enums.TipoImporte;
import com.cnunodevs.serverfinanceapp.service.AhorrosService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ConditionHandler {
    
    private final AhorrosService ahorrosService;

    public Movimiento buildConditionBasedOn(Movimiento movimiento) {
                                        //cambiar nombre metodo
        Ahorro ahorro = ahorrosService.findAhorroAutomaticoDefaultByUsuarioId(movimiento.getUsuario().getId());
        Condicion condicion = ahorro.getCondicion();
        Double ingreso = movimiento.getImporte().doubleValue();
        Double ingresoLuegoDescuento = 0.0;
        if(condicion.getTipoImporte().equals(TipoImporte.EFECTIVO)) {
            ahorro.setImporte(BigDecimal.valueOf(ahorro.getImporte().doubleValue() + condicion.getCantidadDescontar().doubleValue()));
            ingresoLuegoDescuento = ingreso - condicion.getCantidadDescontar().doubleValue();
        }else if(condicion.getTipoImporte().equals(TipoImporte.PORCENTAJE)) {
            BigDecimal percentajeDiscounter = BigDecimal.valueOf(ahorro.getImporte().doubleValue() 
                                            * (condicion.getCantidadDescontar().doubleValue() / 100));
            ahorro.setImporte(ahorro.getImporte().add(percentajeDiscounter));
            ingresoLuegoDescuento = ingreso - percentajeDiscounter.doubleValue();
        }
        movimiento.setImporte(BigDecimal.valueOf(ingresoLuegoDescuento));
        return movimiento;
    }

    public boolean fullfitCondition(BigDecimal importe, Condicion condicion) {
        if(condicion.getExpresion().equals(Expresion.DESCONTAR_MAYOR_IGUAL_A)) {
            return importe.doubleValue() >= condicion.getImporte().doubleValue();
        }
        else if(condicion.getExpresion().equals(Expresion.DESCONTAR_MAYOR_A)) {
            return importe.doubleValue() > condicion.getImporte().doubleValue();
        }
        else if(condicion.getExpresion().equals(Expresion.DESCONTAR_IGUAL_A)) {
            return importe.doubleValue() == condicion.getImporte().doubleValue();
        }
        else if(condicion.getExpresion().equals(Expresion.DESCONTAR_MENOR_IGUAL_A)) {
            return importe.doubleValue() <= condicion.getImporte().doubleValue();
        }
        else if(condicion.getExpresion().equals(Expresion.DESCONTAR_MENOR_A)) {
            return importe.doubleValue() < condicion.getImporte().doubleValue();
        }
        else if(condicion.getExpresion().equals(Expresion.DESCONTAR)) {
            return true;
        }
        else {
            return false;
        }
    }
}
