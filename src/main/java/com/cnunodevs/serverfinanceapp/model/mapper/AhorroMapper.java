package com.cnunodevs.serverfinanceapp.model.mapper;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.cnunodevs.serverfinanceapp.model.dto.AhorroDTO;
import com.cnunodevs.serverfinanceapp.model.dto.CondicionDTO;
import com.cnunodevs.serverfinanceapp.model.dto.ObjetivoDTO;
import com.cnunodevs.serverfinanceapp.model.entity.Ahorro;
import com.cnunodevs.serverfinanceapp.model.entity.Condicion;
import com.cnunodevs.serverfinanceapp.model.entity.Objetivo;
import com.cnunodevs.serverfinanceapp.model.entity.enums.TipoAhorro;

@Service
public class AhorroMapper implements GenericMapper<Ahorro, AhorroDTO>{
    @Override
    public Ahorro dtoToPojo(AhorroDTO dto) {
        Condicion condicion = new CondicionMapper().dtoToPojo(dto.getCondicion());
        Objetivo objetivo = new ObjetivoMapper().dtoToPojo(dto.getObjetivo());
        Ahorro ahorro = Ahorro.builder()
                            .tipo(TipoAhorro.valueOf(dto.getTipo()))
                            .importe(BigDecimal.valueOf(dto.getImporte()))
                            .automatico(dto.isAutomatico())
                            .objetivo(objetivo)
                            .condicion(condicion)
                            .build();

        if(dto.getId() != null){
            ahorro.setId(dto.getId());    
        }
        
        return ahorro;
    }

    @Override
    public AhorroDTO pojoToDto(Ahorro pojo) {
        CondicionDTO condicion = new CondicionMapper().pojoToDto(pojo.getCondicion());
        ObjetivoDTO objetivo = new ObjetivoMapper().pojoToDto(pojo.getObjetivo());
        AhorroDTO ahorroDTO= AhorroDTO.builder()
                            .tipo(pojo.getTipo().toString())
                            .importe(pojo.getImporte().doubleValue())
                            .automatico(pojo.isAutomatico())
                            .objetivo(objetivo)
                            .condicion(condicion)
                            .build();
        return ahorroDTO;
    }
    
}
