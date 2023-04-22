package com.cnunodevs.serverfinanceapp.model.mapper;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.cnunodevs.serverfinanceapp.model.dto.CondicionDTO;
import com.cnunodevs.serverfinanceapp.model.entity.Ahorro;
import com.cnunodevs.serverfinanceapp.model.entity.Condicion;
import com.cnunodevs.serverfinanceapp.model.entity.enums.Expresion;
import com.cnunodevs.serverfinanceapp.model.entity.enums.TipoImporte;

@Service
public class CondicionMapper implements GenericMapper<Condicion, CondicionDTO> {

    @Override
    public Condicion dtoToPojo(CondicionDTO dto) {
        
        if (dto == null) {
            return null;
        }

        Condicion condicion = Condicion.builder()
                                            .id(dto.getId())
                                            .expresion(Expresion.valueOf(dto.getExpresion()))
                                            .importe(BigDecimal.valueOf(dto.getImporte()))
                                            .tipoImporte(TipoImporte.valueOf(dto.getTipoImporte()))
                                            .enabled(dto.getEnabled())
                                            .ahorro(Ahorro.builder().id(dto.getIdAhorro()).build())
                                        .build();
        // if(dto.getId() != null) {
        //     condicion.setId(dto.getId());
        // }

        return condicion;
    }

    @Override
    public CondicionDTO pojoToDto(Condicion pojo) {

        if (pojo == null) {
            return null;
        }

        return CondicionDTO.builder()
                                .expresion(pojo.getExpresion().toString())
                                .importe(pojo.getImporte().doubleValue())
                                .tipoImporte(pojo.getTipoImporte().toString())
                                .enabled(pojo.getEnabled())
                                .idAhorro(pojo.getAhorro().getId())
                            .build();
    }
    
}
