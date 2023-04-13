package com.cnunodevs.serverfinanceapp.model.mapper;

import java.math.BigDecimal;
import java.util.List;

import com.cnunodevs.serverfinanceapp.model.dto.AhorroDTO;
import com.cnunodevs.serverfinanceapp.model.dto.CondicionDTO;
import com.cnunodevs.serverfinanceapp.model.dto.ObjetivoDTO;
import com.cnunodevs.serverfinanceapp.model.entity.Ahorro;
import com.cnunodevs.serverfinanceapp.model.entity.Condicion;
import com.cnunodevs.serverfinanceapp.model.entity.Objetivo;
import com.cnunodevs.serverfinanceapp.model.entity.enums.TipoAhorro;

public class AhorroMapper implements GenericMapper<Ahorro, AhorroDTO>{
    @Override
    public Ahorro dtoToPojo(AhorroDTO dto) {
        Condicion condicion = new CondicionMapper().dtoToPojo(dto.getCondicion());
        List<Objetivo> objetivos = dto.getObjetivos()
                                        .stream()
                                        .map(objetivo -> new ObjetivoMapper()
                                                                .dtoToPojo(objetivo))
                                        .toList();
        Ahorro ahorro = Ahorro.builder()
                            .tipo(TipoAhorro.valueOf(dto.getTipo()))
                            .importe(BigDecimal.valueOf(dto.getImporte()))
                            .automatico(dto.isAutomatico())
                            .objetivos(objetivos)
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
        List<ObjetivoDTO> objetivos = pojo.getObjetivos()
                                        .stream()
                                        .map(objetivo -> new ObjetivoMapper()
                                                                .pojoToDto(objetivo))
                                        .toList();
        AhorroDTO ahorroDTO= AhorroDTO.builder()
                            .tipo(pojo.getTipo().toString())
                            .importe(pojo.getImporte().doubleValue())
                            .automatico(pojo.isAutomatico())
                            .objetivos(objetivos)
                            .condicion(condicion)
                            .build();
        return ahorroDTO;
    }
    
}
