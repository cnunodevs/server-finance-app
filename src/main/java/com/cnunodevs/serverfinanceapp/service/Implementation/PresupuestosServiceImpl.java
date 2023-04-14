package com.cnunodevs.serverfinanceapp.service.Implementation;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cnunodevs.serverfinanceapp.model.domain.MetricaPresupuesto;
import com.cnunodevs.serverfinanceapp.model.entity.Movimiento;
import com.cnunodevs.serverfinanceapp.model.entity.Presupuesto;
import com.cnunodevs.serverfinanceapp.model.entity.Usuario;
import com.cnunodevs.serverfinanceapp.repository.PresupuestosRepository;
import com.cnunodevs.serverfinanceapp.service.PresupuestosService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PresupuestosServiceImpl implements PresupuestosService {

    private final PresupuestosRepository presupuestosRepository;

    @Override
    public boolean presupuestoAlreadyExist(UUID idPresupuesto) {
        return presupuestosRepository.existsById(idPresupuesto);
    }

    @Override
    public MetricaPresupuesto getMetricaPresupuestoByMovimientos(Set<Movimiento> movimientos) {
        return new MetricaPresupuesto(movimientos);
    }

    @Override
    public List<Presupuesto> getPresupuestosByUsuario(Usuario usuario) {
        Example<Presupuesto> example = Example.of(Presupuesto.builder().usuario(usuario).build());
        return presupuestosRepository.findAll(example);
    }

    @Override
    public List<MetricaPresupuesto> getMetricasPresupuestos(Set<Presupuesto> presupuestos) {
        return presupuestos.stream().map(p -> new MetricaPresupuesto(p.getMovimientos())).toList();
    }

    @Override
    public Page<Presupuesto> getPresupuestosPaginate(Pageable paging) {
        return presupuestosRepository.findAll(paging);
    }

    @Override
    public Optional<Presupuesto> getPresupuestoById(UUID idPresupuesto) {
        return presupuestosRepository.findById(idPresupuesto);
    }

    @Override
    public void createPresupuesto(Presupuesto presupuesto) {
        presupuestosRepository.save(presupuesto);
    }

    @Override
    public boolean similarAlreadyExist(String nombre, UUID idUsuario) {
        Example<Presupuesto> example = Example
                .of(Presupuesto.builder().nombre(nombre).usuario(Usuario.builder().id(idUsuario).build()).build());
        return presupuestosRepository.findOne(example).isPresent();
    }

    @Override
    public void updatePresupuesto(Presupuesto presupuesto) {
        presupuestosRepository.save(presupuesto);
    }

    @Override
    public void deletePresupuestoById(UUID id) {
        presupuestosRepository.deleteById(id);
    }
    
}
