package com.cnunodevs.serverfinanceapp.service.Implementation;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cnunodevs.serverfinanceapp.model.domain.MetricaBalance;
import com.cnunodevs.serverfinanceapp.model.entity.Balance;
import com.cnunodevs.serverfinanceapp.model.entity.Movimiento;
import com.cnunodevs.serverfinanceapp.model.entity.Presupuesto;
import com.cnunodevs.serverfinanceapp.model.entity.Usuario;
import com.cnunodevs.serverfinanceapp.model.entity.enums.TipoMovimiento;
import com.cnunodevs.serverfinanceapp.repository.MovimientosRepository;
import com.cnunodevs.serverfinanceapp.service.BalanceService;
import com.cnunodevs.serverfinanceapp.service.CondicionesService;
import com.cnunodevs.serverfinanceapp.service.MovimientosService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Primary
@Transactional
@RequiredArgsConstructor
public class MovimientosServiceImpl implements MovimientosService {

    private final MovimientosRepository movimientosRepository;
    private final CondicionesService condicionesService;
    private final BalanceService balanceService;
    
    @Override
    public List<Movimiento> findMovimientosByPresupuestoId(UUID idPresupuesto) {
        Example<Movimiento> example = Example
                .of(Movimiento.builder().presupuesto(Presupuesto.builder().id(idPresupuesto).build()).build());
        return movimientosRepository.findAll(example);
    }

    @Override
    public MetricaBalance getMetricaBalanceByUsuario(UUID idUsuario) {
        Example<Movimiento> example = Example
                .of(Movimiento.builder().usuario(Usuario.builder().id(idUsuario).build()).build());
        List<Movimiento> movimientos = movimientosRepository.findAll(example);
        Balance balance = balanceService.getBalanceByUsuario(idUsuario);
        return MetricaBalance.getMetricaBalance(balance.getBalance().doubleValue(), new HashSet<Movimiento>(movimientos));
    }

    @Override
    public Page<Movimiento> getMovimientosPaginateByUsuario(Pageable paging, Example<Movimiento> example) {
        return movimientosRepository.findAll(example, paging);
    }

    @Override
    public boolean movimientoAlreadyExist(UUID idMovimiento) {
        return movimientosRepository.existsById(idMovimiento);
    }

    @Override
    public Optional<Movimiento> getMovimientoById(UUID idMovimiento) {
        return movimientosRepository.findById(idMovimiento);
    }

    @Override
    public void deleteMovimientoById(UUID id) {
        movimientosRepository.deleteById(id);
    }

    @Override
    public void deleteAllMovimientosById(List<UUID> idsMovimientos) {
        movimientosRepository.deleteAllById(idsMovimientos);
    }

    @Override
    public void deleteAllMovimientos(List<Movimiento> movimientos) {
        movimientosRepository.deleteAll(movimientos);
    }

    @Override
    public Page<Movimiento> getMovimientosPaginateByPortafolio(Pageable paging, Example<Movimiento> example) {
        return movimientosRepository.findAll(example, paging);
    }

    @Override
    public void createMovimiento(Movimiento movimiento) {

        double importeDescuento = 0.0;
        if (movimiento.getTipo().equals(TipoMovimiento.INGRESO)) {
            Movimiento movimientoConDescuento = condicionesService.applyCondicionIfExist(movimiento);
            importeDescuento = movimiento.getImporte().doubleValue() - movimientoConDescuento.getImporte().doubleValue();
        } 
        movimientosRepository.save(movimiento);
        if (movimiento.getTipo().equals(TipoMovimiento.INGRESO)) {
            balanceService.aumentarBalanceByUsuario(movimiento.getImporte(), movimiento.getUsuario().getId());
        } else {
            balanceService.disminuirBalanceByUsuario(movimiento.getImporte(), movimiento.getUsuario().getId());
        }
        if (importeDescuento != 0.0) {
            crearMovimientoDesdeDisponible(BigDecimal.valueOf(importeDescuento), movimiento.getUsuario().getId(), "ahorro", "logo_ahorro");
            balanceService.disminuirBalanceByUsuario(BigDecimal.valueOf(importeDescuento), movimiento.getUsuario().getId());
        }         
        
    }

    @Override
    public void crearMovimientoHaciaDisponible(BigDecimal importe, UUID idUsuario, String concepto, String logoConcepto) {
        //Aumenta el monto en balance
        //Registra movimiento de transferencia hacia disponible
        Movimiento movimiento = Movimiento.builder()
                                        .importe(importe)
                                        .tipo(TipoMovimiento.TRANSFERENCIA_HACIA_DISPONIBLE)
                                        .concepto(concepto)
                                        .logoConcepto(logoConcepto)
                                        .usuario(Usuario.builder().id(idUsuario).build())
                                        .presupuesto(null)
                                        .contabilizable(true)
                                        .build();
        balanceService.aumentarBalanceByUsuario(importe, idUsuario);
        movimientosRepository.save(movimiento);
    }

    @Override
    public void crearMovimientoDesdeDisponible(BigDecimal importe, UUID idUsuario, String concepto, String logoConcepto) {
        //Disminuye el monto en balance
        //Registra movimiento de transferencia desde disponible
        Movimiento movimiento = Movimiento.builder()
                                        .importe(importe)
                                        .tipo(TipoMovimiento.TRANSFERENCIA_DESDE_DISPONIBLE)
                                        .concepto(concepto)
                                        .logoConcepto(logoConcepto)
                                        .usuario(Usuario.builder().id(idUsuario).build())
                                        .presupuesto(null)
                                        .contabilizable(true)
                                        .build();
        balanceService.disminuirBalanceByUsuario(importe, idUsuario);
        movimientosRepository.save(movimiento);
        balanceService.aumentarBalanceByUsuario(movimiento.getImporte(), movimiento.getUsuario().getId());
    }

    @Override
    public void createMovimientoDescuentoACuentaEspecifica(Movimiento movimiento, UUID idCuentaAhorroEspecifica) {
        movimientosRepository.save(movimiento);
        Movimiento movimientoConDescuento = condicionesService.applyCondicionToSpecificAhorro(movimiento, idCuentaAhorroEspecifica);
        double importeDescuento = movimiento.getImporte().doubleValue() - movimientoConDescuento.getImporte().doubleValue();
        crearMovimientoDesdeDisponible(BigDecimal.valueOf(importeDescuento), movimiento.getUsuario().getId(), "ahorro", "logo_ahorro");
        balanceService.disminuirBalanceByUsuario(movimiento.getImporte(), movimiento.getUsuario().getId());
    }

    @Override
    public void createMovimientoOfPresupuesto(Movimiento movimiento) {
        movimientosRepository.save(movimiento);
    }

    @Override
    public void crearMovimientoHaciaDisponibleDesdeAhorro(BigDecimal importe, UUID idUsuario) {
        crearMovimientoHaciaDisponible(importe, idUsuario, "ahorro", "logo_ahorro");
    }

    @Override
    public void crearMovimientoDesdeDisponibleParaAhorrro(BigDecimal importe, UUID idUsuario) {
       crearMovimientoDesdeDisponible(importe, idUsuario, "ahorro", "logo_ahorro");
    }

    @Override
    public Boolean hasAnyMovimientoByUsuario(UUID idUsuario) {
        Example<Movimiento> example = Example.of(Movimiento.builder().contabilizable(true).usuario(Usuario.builder().id(idUsuario).build()).build());
        return !movimientosRepository.findAll(example).isEmpty();
    }

    @Override
    public Boolean hasAnyMovimientoByPresupuesto(UUID idPresupuesto) {
        Example<Movimiento> example = Example.of(Movimiento.builder().contabilizable(false).presupuesto(Presupuesto.builder().id(idPresupuesto).build()).build());
        return !movimientosRepository.findAll(example).isEmpty();
    }
    
}
