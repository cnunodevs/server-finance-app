package com.cnunodevs.serverfinanceapp.service.Implementation;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.cnunodevs.serverfinanceapp.model.domain.ConditionHandler;
import com.cnunodevs.serverfinanceapp.model.entity.Ahorro;
import com.cnunodevs.serverfinanceapp.model.entity.Condicion;
import com.cnunodevs.serverfinanceapp.model.entity.Movimiento;
import com.cnunodevs.serverfinanceapp.repository.CondicionRepository;
import com.cnunodevs.serverfinanceapp.service.AhorrosService;
import com.cnunodevs.serverfinanceapp.service.CondicionesService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CondicionesServiceImpl implements CondicionesService {

    private final ConditionHandler conditionHandler;
    private final CondicionRepository conditionRepository;
    private final AhorrosService ahorroService;


    @Override
    public void save(Condicion condicion) {
        conditionRepository.save(condicion);
    }

    @Override
    public Movimiento applyCondicionIfExist(Movimiento movimiento) {
        if(!conditionHandler.fullfitCondition(movimiento.getImporte(),
            ahorroService
                    .findAhorroAutomaticoDefaultByUsuarioId(movimiento
                                                                .getUsuario()
                                                                .getId())
                    .getCondicion()))
        {
            movimiento.setImporte(BigDecimal.valueOf(0.0));
        }else{
            movimiento.setImporte(conditionHandler.buildConditionBasedOn(movimiento.getImporte(),
            ahorroService.findAhorroAutomaticoDefaultByUsuarioId(movimiento.getUsuario().getId())));
        }
        return movimiento;
    }

    @Override
    public Movimiento applyCondicionToSpecificAhorro(Movimiento movimiento, UUID ahorroID) {
        Ahorro ahorro = ahorroService.findAhorroById(ahorroID).get();
        if(!conditionHandler.fullfitCondition(movimiento.getImporte(),
                 ahorro.getCondicion())) 
        {
            movimiento.setImporte(BigDecimal.valueOf(0.0));
        }else{
            movimiento.setImporte(conditionHandler.buildConditionBasedOn(movimiento.getImporte(), ahorro));
        }
        return movimiento;
    }

    @Override
    public void deleteCondicion(UUID ahorroID) {
        Example<Condicion> exampleCondicion = Example.of(Condicion.builder()
                                                                  .ahorro(Ahorro.builder()
                                                                                .id(ahorroID)
                                                                                .build())
                                                                  .build());
        conditionRepository.delete(conditionRepository.findOne(exampleCondicion).get());
    }

    
    
    
}
