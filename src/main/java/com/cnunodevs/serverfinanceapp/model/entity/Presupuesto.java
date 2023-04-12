package com.cnunodevs.serverfinanceapp.model.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
@Table(name="presupuestos")
public class Presupuesto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(length = 50)
    private String nombre;
    
    @Column(length = 255)
    private String descripcion;
    
    @Column(length = 50)
    private String periodo;
    
    @CreationTimestamp
    private LocalDateTime creationDateTime;
    
    @UpdateTimestamp
    private LocalDateTime lastModified;

    @ManyToOne
    @JoinColumn(name = "usuario_fk")
    private Usuario usuario;

    @ManyToMany
    @JoinTable(
        name = "montos_presupuestos",
        joinColumns = @JoinColumn(name = "presupuesto_fk"),
        inverseJoinColumns = @JoinColumn(name = "movimiento_fk")
    )
    private List<Movimiento> movimientos;
}
