package com.cnunodevs.serverfinanceapp.model.mapper;

import java.math.BigDecimal;

import com.cnunodevs.serverfinanceapp.model.dto.InversionDTO;
import com.cnunodevs.serverfinanceapp.model.entity.Inversion;
import com.cnunodevs.serverfinanceapp.model.entity.Portafolio;
import com.cnunodevs.serverfinanceapp.model.entity.enums.PerfilRiesgo;
import com.cnunodevs.serverfinanceapp.model.entity.enums.PlazoInversion;
import com.cnunodevs.serverfinanceapp.model.entity.enums.SectorActivo;
import com.cnunodevs.serverfinanceapp.model.entity.enums.TipoActivo;

public class InversionMapper implements GenericMapper<Inversion, InversionDTO> {

    @Override
    public Inversion dtoToPojo(InversionDTO dto) {
        Inversion inversion = Inversion.builder()
                .portafolio(Portafolio.builder().id(dto.getIdPortafolio()).build())
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .precio(BigDecimal.valueOf(dto.getPrecio()))
                .cantidad(dto.getCantidad())
                .plazo(PlazoInversion.valueOf(dto.getPlazo()))
                .perfilRiesgo(PerfilRiesgo.valueOf(dto.getPerfilRiesgo()))
                .tipo(TipoActivo.valueOf(dto.getTipo()))
                .sector(SectorActivo.valueOf(dto.getSector()))
                .rentabilidadEsperada(BigDecimal.valueOf(dto.getRentabilidadEsperada()))
                .build();
        if (dto.getId() != null) {
            inversion.setId(dto.getId());
        }
        return inversion;
    }

    @Override
    public InversionDTO pojoToDto(Inversion pojo) {
        InversionDTO inversionDTO = InversionDTO.builder()
                .id(pojo.getId())
                .idPortafolio(pojo.getPortafolio().getId())
                .nombre(pojo.getNombre())
                .descripcion(pojo.getDescripcion())
                .precio(pojo.getPrecio().doubleValue())
                .cantidad(pojo.getCantidad())
                .plazo(pojo.getPlazo().toString())
                .perfilRiesgo(pojo.getPerfilRiesgo().toString())
                .tipo(pojo.getTipo().toString())
                .sector(pojo.getSector().toString())
                .rentabilidadEsperada(pojo.getRentabilidadEsperada().doubleValue())
                .build();
        return inversionDTO;
    }

}
