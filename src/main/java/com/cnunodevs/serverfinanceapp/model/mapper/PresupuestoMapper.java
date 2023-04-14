package com.cnunodevs.serverfinanceapp.model.mapper;

import org.springframework.stereotype.Service;

import com.cnunodevs.serverfinanceapp.model.dto.PresupuestoDTO;
import com.cnunodevs.serverfinanceapp.model.entity.Presupuesto;
import com.cnunodevs.serverfinanceapp.model.entity.enums.PeriodoPresupuesto;

@Service
public class PresupuestoMapper implements GenericMapper<Presupuesto, PresupuestoDTO> {

    @Override
    public Presupuesto dtoToPojo(PresupuestoDTO dto) {
        Presupuesto presupuesto = Presupuesto.builder()
                                            .nombre(dto.getNombre())
                                            .descripcion(dto.getDescripcion())
                                            .periodo(PeriodoPresupuesto.valueOf(dto.getPeriodo()))
                                            .build();
        if (dto.getId() != null) {
            presupuesto.setId(dto.getId());
        }
        return presupuesto;
    }

    @Override
    public PresupuestoDTO pojoToDto(Presupuesto pojo) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'pojoToDto'");
    }
    
}
