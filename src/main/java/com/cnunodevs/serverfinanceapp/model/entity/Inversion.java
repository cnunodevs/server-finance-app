package com.cnunodevs.serverfinanceapp.model.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.cnunodevs.serverfinanceapp.model.entity.enums.PerfilRiesgo;
import com.cnunodevs.serverfinanceapp.model.entity.enums.PlazoInversion;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Column(length = 100)
    @Enumerated(EnumType.STRING)
    private PlazoInversion plazo;

    private String tasaInteres;

    @Column(length = 50)
    private PerfilRiesgo perfilRiesgo;

    private boolean simulada;

    
    @ManyToOne
    @JoinColumn(name = "portafolio_fk")
    private Portafolio portafolio;
    
    @OneToOne
    @JoinColumn(name = "portafolio_fk")
    private Activo activo;
    
    @CreationTimestamp
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    private LocalDateTime ultimaActualizacion;

}
