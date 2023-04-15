package com.cnunodevs.serverfinanceapp.service;

import java.math.BigDecimal;
import java.util.UUID;

import com.cnunodevs.serverfinanceapp.model.entity.Balance;

public interface BalanceService {
    
    Balance getBalanceByUsuario(UUID idUsuario);
    void aumentarBalanceByUsuario(BigDecimal importe, UUID idUsuario);
    void disminuirBalanceByUsuario(BigDecimal importe, UUID idUsuario);

}
