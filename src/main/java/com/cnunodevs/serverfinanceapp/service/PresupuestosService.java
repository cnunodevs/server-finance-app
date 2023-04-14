package com.cnunodevs.serverfinanceapp.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cnunodevs.serverfinanceapp.model.domain.MetricaPresupuesto;
import com.cnunodevs.serverfinanceapp.model.entity.Movimiento;
import com.cnunodevs.serverfinanceapp.model.entity.Presupuesto;
import com.cnunodevs.serverfinanceapp.model.entity.Usuario;

public interface PresupuestosService {

    boolean presupuestoAlreadyExist(UUID idPresupuesto);

    MetricaPresupuesto getMetricaPresupuestoByMovimientos(Set<Movimiento> movimientos);

    List<Presupuesto> getPresupuestosByUsuario(Usuario usuario);

    List<MetricaPresupuesto> getMetricasPresupuestos(Set<Presupuesto> presupuestos);

    Page<Presupuesto> getPresupuestosPaginate(Pageable paging);

    Optional<Presupuesto> getPresupuestoById(UUID idPresupuesto);

    void createPresupuesto(Presupuesto presupuesto);

    boolean similarAlreadyExist(String nombre, UUID idUsuario);

    void updatePresupuesto(Presupuesto presupuesto);

    void deletePresupuestoById(UUID id);
    
}
