package com.cnunodevs.serverfinanceapp.model.mapper;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.cnunodevs.serverfinanceapp.model.dto.AhorroDTO;
import com.cnunodevs.serverfinanceapp.model.entity.Ahorro;
import com.cnunodevs.serverfinanceapp.model.entity.Condicion;
import com.cnunodevs.serverfinanceapp.model.entity.Objetivo;
import com.cnunodevs.serverfinanceapp.model.entity.enums.TipoAhorro;

@Service
public class AhorroMapper implements GenericMapper<Ahorro, AhorroDTO>{
    @Override
    public Ahorro dtoToPojo(AhorroDTO dto) {
        Ahorro ahorro = Ahorro.builder()
                            .tipo(TipoAhorro.valueOf(dto.getTipo()))
                            .importe(BigDecimal.valueOf(dto.getImporte()))
                            .automatico(dto.isAutomatico())
                            .objetivo(Objetivo.builder().id(dto.getIdObjetivo()).build())
                            .condicion(Condicion.builder().id(dto.getIdCondicion()).build())
                            .build();

        if(dto.getId() != null){
            ahorro.setId(dto.getId());    
        }
        
        return ahorro;
    }

    @Override
    public AhorroDTO pojoToDto(Ahorro pojo) {
        AhorroDTO ahorroDTO= AhorroDTO.builder()
                            .tipo(pojo.getTipo().toString())
                            .importe(pojo.getImporte().doubleValue())
                            .automatico(pojo.isAutomatico())
                            .idObjetivo(pojo.getObjetivo().getId())
                            .idCondicion(pojo.getCondicion().getId())
                            .build();
        return ahorroDTO;
    }
    
}
