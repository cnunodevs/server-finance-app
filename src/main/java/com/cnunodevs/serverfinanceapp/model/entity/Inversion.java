package com.cnunodevs.serverfinanceapp.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.cnunodevs.serverfinanceapp.model.entity.enums.PerfilRiesgo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name="inversiones")
public class Inversion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private BigDecimal rentabilidad;

    // to change
    @Column(length = 50)
    private String plazo;
    // to change
    @Column(length = 10)
    private String periodicidad;

    @Column(length = 25)
    private PerfilRiesgo perfilRiesgo;

    private boolean simulada;

    @CreationTimestamp
    private LocalDateTime creationDateTime;
    
    @UpdateTimestamp
    private LocalDateTime lastModified;

    @ManyToOne
    @JoinColumn(name = "portafolio_fk")
    private Portafolio portafolio;

    @OneToOne
    @JoinColumn(name = "portafolio_fk")
    private Activos activo;
}
