package com.cnunodevs.serverfinanceapp.model.entity;

import java.math.BigDecimal;
import java.util.UUID;

import com.cnunodevs.serverfinanceapp.model.entity.enums.TipoAhorro;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="ahorros")
public class Ahorro {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(length = 50)
    @Enumerated(EnumType.STRING)
    private TipoAhorro tipo;

    private BigDecimal importe;

    private boolean automatico;

    @ManyToMany
    @JoinTable(
        name = "ahorro_objetivo",
        joinColumns = @JoinColumn(name = "ahorro_fk"),
        inverseJoinColumns = @JoinColumn(name = "objetivo_fk")
    )
    private Objetivo objetivo;

    @OneToOne(mappedBy = "ahorro_fk")
    private Condicion condicion;
    
    @ManyToOne
    @JoinColumn(name = "usuario_fk")
    private Usuario usuario;
}
