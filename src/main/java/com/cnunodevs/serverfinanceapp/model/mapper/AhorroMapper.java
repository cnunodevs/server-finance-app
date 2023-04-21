package com.cnunodevs.serverfinanceapp.model.mapper;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.cnunodevs.serverfinanceapp.model.dto.AhorroDTO;
import com.cnunodevs.serverfinanceapp.model.entity.Ahorro;
import com.cnunodevs.serverfinanceapp.model.entity.Objetivo;
import com.cnunodevs.serverfinanceapp.model.entity.Usuario;
import com.cnunodevs.serverfinanceapp.model.entity.enums.TipoAhorro;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AhorroMapper implements GenericMapper<Ahorro, AhorroDTO>{

    private final CondicionMapper condicionMapper;

    @Override
    public Ahorro dtoToPojo(AhorroDTO dto) {
        Ahorro ahorro = Ahorro.builder()
                            .tipo(TipoAhorro.valueOf(dto.getTipo()))
                            .importe(BigDecimal.valueOf(dto.getImporte()))
                            .automatico(dto.isAutomatico())
                            .objetivo(Objetivo.builder().id(dto.getIdObjetivo()).build())
                            .usuario(Usuario.builder().id(dto.getIdUsuario()).build())
                            .condicion(condicionMapper.dtoToPojo(dto.getCondicionDTO()))
                            .build();

        if(dto.getId() != null){
            ahorro.setId(dto.getId());    
        }
        
        return ahorro;
    }

    @Override
    public AhorroDTO pojoToDto(Ahorro pojo) {
        AhorroDTO ahorroDTO= AhorroDTO.builder()
                            .id(pojo.getId())
                            .nombre(pojo.getNombre())
                            .descripcion(pojo.getDescripcion())
                            .tipo(pojo.getTipo().toString())
                            .importe(pojo.getImporte().doubleValue())
                            .automatico(pojo.isAutomatico())
                            .idObjetivo(pojo.getObjetivo().getId())
                            .idUsuario(pojo.getUsuario().getId())
                            .condicionDTO(condicionMapper.pojoToDto(pojo.getCondicion()))
                            .build();
        return ahorroDTO;
    }
    
}
