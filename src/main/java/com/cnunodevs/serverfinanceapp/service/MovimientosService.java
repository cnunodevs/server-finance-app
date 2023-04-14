package com.cnunodevs.serverfinanceapp.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.cnunodevs.serverfinanceapp.model.domain.MetricaBalance;
import com.cnunodevs.serverfinanceapp.model.entity.Movimiento;

public interface MovimientosService {

    List<Movimiento> findMovimientosByPresupuestoId(UUID idPresupuesto);

    MetricaBalance getMetricaBalanceByUsuario(UUID idUsuario);

    Page<Movimiento> getMovimientosPaginateByUsuario(Pageable paging, Example<Movimiento> example);

    boolean movimientoAlreadyExist(UUID idMovimiento);

    Optional<Movimiento> getMovimientoById(UUID idMovimiento);
    
}