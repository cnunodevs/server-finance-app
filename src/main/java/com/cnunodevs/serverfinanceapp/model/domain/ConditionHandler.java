package com.cnunodevs.serverfinanceapp.model.domain;

import java.math.BigDecimal;

import com.cnunodevs.serverfinanceapp.model.entity.Ahorro;
import com.cnunodevs.serverfinanceapp.model.entity.Condicion;
import com.cnunodevs.serverfinanceapp.model.entity.enums.Expresion;
import com.cnunodevs.serverfinanceapp.model.entity.enums.TipoImporte;


public class ConditionHandler {
    
    public static BigDecimal buildConditionBasedOn(Ahorro ahorro, BigDecimal monto/*aun nose q nombre ponerle */) {
        BigDecimal newMonto = null;
        Condicion condicion = ahorro.getCondicion();
        if(condicion.getTipoImporte().equals(TipoImporte.EFECTIVO)) {
            ahorro.setImporte(ahorro.getImporte().add(BigDecimal.valueOf(condicion.getCantidadDescontar())));
            newMonto = monto.subtract(BigDecimal.valueOf(condicion.getCantidadDescontar()));
        }else if(condicion.getTipoImporte().equals(TipoImporte.PORCENTAJE)) {
            BigDecimal percentajeDiscounter = BigDecimal.valueOf(ahorro.getImporte().doubleValue() 
                                            * (condicion.getCantidadDescontar().doubleValue() / 100));
            ahorro.setImporte(ahorro.getImporte().add(percentajeDiscounter));
            newMonto = monto.subtract(percentajeDiscounter);
        }
        return newMonto;
    }

    public static boolean fullfitCondition(Ahorro ahorro, Condicion condicion) {
        Double importe = ahorro.getImporte().doubleValue();
        if(condicion.getExpresion().equals(Expresion.DESCONTAR_MAYOR_IGUAL_A)) {
            return importe >= condicion.getImporte().doubleValue();
        }
        else if(condicion.getExpresion().equals(Expresion.DESCONTAR_MAYOR_A)) {
            return importe > condicion.getImporte().doubleValue();
        }
        else if(condicion.getExpresion().equals(Expresion.DESCONTAR_IGUAL_A)) {
            return importe == condicion.getImporte().doubleValue();
        }
        else if(condicion.getExpresion().equals(Expresion.DESCONTAR_MENOR_IGUAL_A)) {
            return importe <= condicion.getImporte().doubleValue();
        }
        else if(condicion.getExpresion().equals(Expresion.DESCONTAR_MENOR_A)) {
            return importe < condicion.getImporte().doubleValue();
        }
        else if(condicion.getExpresion().equals(Expresion.DESCONTAR)) {
            return true;
        }
        else {
            return false;
        }
    }
}
