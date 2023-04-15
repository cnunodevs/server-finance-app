package com.cnunodevs.serverfinanceapp.model.mapper;

import java.math.BigDecimal;

import com.cnunodevs.serverfinanceapp.model.dto.CondicionDTO;
import com.cnunodevs.serverfinanceapp.model.entity.Ahorro;
import com.cnunodevs.serverfinanceapp.model.entity.Condicion;
import com.cnunodevs.serverfinanceapp.model.entity.enums.Expresion;
import com.cnunodevs.serverfinanceapp.model.entity.enums.TipoImporte;

public class CondicionMapper implements GenericMapper<Condicion, CondicionDTO> {

    @Override
    public Condicion dtoToPojo(CondicionDTO dto) {
        
        Condicion condicion = Condicion.builder()
                                            .expresion(Expresion.valueOf(dto.getExpresion()))
                                            .importe(BigDecimal.valueOf(dto.getImporte()))
                                            .tipoImporte(TipoImporte.valueOf(dto.getTipoImporte()))
                                            .enabled(dto.getEnabled())
                                            .ahorro(Ahorro.builder().id(dto.getAhorroId()).build())
                                        .build();
        if(dto.getId() != null) {
            condicion.setId(dto.getId());
        }

        return condicion;
    }

    @Override
    public CondicionDTO pojoToDto(Condicion pojo) {
        return CondicionDTO.builder()
                                .expresion(pojo.getExpresion().toString())
                                .importe(pojo.getImporte().doubleValue())
                                .tipoImporte(pojo.getTipoImporte().toString())
                                .enabled(pojo.getEnabled())
                                .ahorroId(pojo.getAhorro().getId())
                            .build();
    }
    
}
