package com.cnunodevs.serverfinanceapp.model.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.cnunodevs.serverfinanceapp.model.entity.enums.TipoAhorro;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
    private TipoAhorro tipo;

    private BigDecimal importe;

    private BigDecimal automatico;

    @ManyToMany
    @JoinTable(
        name = "ahorro_objetivo",
        joinColumns = @JoinColumn(name = "ahorro_fk"),
        inverseJoinColumns = @JoinColumn(name = "objetivo_fk")
    )
    private List<Objetivo> objetivos;

    @OneToOne
    @JoinColumn(name = "condicion_fk")
    private Condicion condicion;
    
}
