package com.cnunodevs.serverfinanceapp.service.Implementation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cnunodevs.serverfinanceapp.model.domain.MetricaBalance;
import com.cnunodevs.serverfinanceapp.model.entity.Movimiento;
import com.cnunodevs.serverfinanceapp.service.MovimientosService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Primary
@Transactional
@RequiredArgsConstructor
public class MovimientosServiceImpl implements MovimientosService {
    
    @Override
    public List<Movimiento> findMovimientosByPresupuestoId(UUID idPresupuesto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findMovimientosByPresupuestoId'");
    }

    @Override
    public MetricaBalance getMetricaBalanceByUsuario(UUID idUsuario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMetricaBalanceByUsuario'");
    }

    @Override
    public Page<Movimiento> getMovimientosPaginateByUsuario(Pageable paging, Example<Movimiento> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMovimientosPaginateByUsuario'");
    }

    @Override
    public boolean movimientoAlreadyExist(UUID idMovimiento) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'movimientoAlreadyExist'");
    }

    @Override
    public Optional<Movimiento> getMovimientoById(UUID idMovimiento) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMovimientoById'");
    }
    
}
