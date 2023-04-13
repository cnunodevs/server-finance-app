package com.cnunodevs.serverfinanceapp.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.cnunodevs.serverfinanceapp.model.entity.enums.PerfilRiesgo;
import com.cnunodevs.serverfinanceapp.model.entity.enums.PlazoInversion;
import com.cnunodevs.serverfinanceapp.model.entity.enums.SectorActivo;
import com.cnunodevs.serverfinanceapp.model.entity.enums.TipoActivo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name="inversiones", indexes = @Index(name="inversion_unique", columnList = "nombre, portafolio_fk", unique = true))
public class Inversion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(cascade=CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinColumn(name = "portafolio_fk")
    private Portafolio portafolio;

    @Column(length = 50, nullable = false)
    private String nombre;
    
    @Column(length = 255)
    private String descripcion;

    @Column(nullable = false, precision = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private Double cantidad;
    
    @Column(length = 100, nullable = false)
    @Enumerated(EnumType.STRING)
    private PlazoInversion plazo;

    @Column(length = 25, nullable = false)
    private PerfilRiesgo perfilRiesgo;

    @Column(length = 50, nullable = false)
    private TipoActivo tipo;

    @Column(length = 50, nullable = false)
    private SectorActivo sector;
    
    @Column(nullable = false, precision = 2)
    private BigDecimal rentabilidadEsperada;
    
    @CreationTimestamp
    private LocalDateTime creationDateTime;
    
    @UpdateTimestamp
    private LocalDateTime lastModified;

}
