package com.cnunodevs.serverfinanceapp.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name="portafolio")
public class Portafolio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(length = 50)
    private String nombre;
    
    @Column(length = 255)
    private String descripcion;

    private BigDecimal valorTotal;

    private BigDecimal rentabilidadPromedio;

    @Column(length = 25)
    private PerfilRiesgo perfilRiesgo;

    @CreationTimestamp
    private LocalDateTime creationDateTime;
    
    @UpdateTimestamp
    private LocalDateTime lastModified;

    @ManyToOne
    @JoinColumn(name = "usuario_fk")
    private Usuario usuario;

    @ManyToMany
    @JoinTable(
        name = "portafolio_objetivos",
        joinColumns =  @JoinColumn(name = "portafolio_fk"),
        inverseJoinColumns = @JoinColumn(name = "objetivos_fk")
    )
    private List<Objetivo> objetivos;
}
