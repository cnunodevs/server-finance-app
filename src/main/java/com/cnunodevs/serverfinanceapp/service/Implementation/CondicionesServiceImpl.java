package com.cnunodevs.serverfinanceapp.service.Implementation;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.cnunodevs.serverfinanceapp.model.domain.ConditionHandler;
import com.cnunodevs.serverfinanceapp.model.entity.Ahorro;
import com.cnunodevs.serverfinanceapp.model.entity.Condicion;
import com.cnunodevs.serverfinanceapp.model.entity.Movimiento;
import com.cnunodevs.serverfinanceapp.repository.AhorroRepository;
import com.cnunodevs.serverfinanceapp.repository.CondicionRepository;
import com.cnunodevs.serverfinanceapp.service.CondicionesService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CondicionesServiceImpl implements CondicionesService {

    private final ConditionHandler conditionHandler;
    private final CondicionRepository conditionRepository;
    private final AhorroRepository ahorroRepository;


    @Override
    public void save(Condicion condicion) {
        conditionRepository.save(condicion);
    }

    @Override
    public Movimiento applyCondicionIfExist(Movimiento movimiento) {
        if(!conditionHandler.fullfitCondition(movimiento.getImporte(),
            ahorroRepository
                    .findAhorrosAutomaticosByUsuarioId(movimiento
                                                                .getUsuario()
                                                                .getId())
                                                                .stream()
                                                                .findFirst()
                                                                .get()
                    .getCondicion()))
        {
            movimiento.setImporte(BigDecimal.valueOf(0.0));
        }else{
            movimiento.setImporte(conditionHandler.buildConditionBasedOn(movimiento.getImporte(),
            ahorroRepository.findAhorrosAutomaticosByUsuarioId(movimiento.getUsuario().getId())
                                                                                        .stream()
                                                                                        .findFirst()
                                                                                        .get()));
        }
        return movimiento;
    }

    @Override
    public Movimiento applyCondicionToSpecificAhorro(Movimiento movimiento, UUID idAhorro) {
        Ahorro ahorro = ahorroRepository.findById(idAhorro).get();
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
    public void deleteCondicion(UUID idAhorro) {
        Example<Condicion> exampleCondicion = Example.of(Condicion.builder()
                                                                  .ahorro(Ahorro.builder()
                                                                                .id(idAhorro)
                                                                                .build())
                                                                  .build());                                                                                                                                    
        conditionRepository.delete(conditionRepository.findOne(exampleCondicion).get());
    }

    
    
    
}
