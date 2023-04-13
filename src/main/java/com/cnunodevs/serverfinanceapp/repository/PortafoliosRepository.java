package com.cnunodevs.serverfinanceapp.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cnunodevs.serverfinanceapp.model.entity.Portafolio;
import com.cnunodevs.serverfinanceapp.model.entity.Usuario;

public interface PortafoliosRepository extends JpaRepository<Portafolio, UUID> {

    Optional<Portafolio> findByNombreAndUsuario(String nombre, Usuario usuario);

    List<Portafolio> findAllByUsuario(Usuario usuario);

}
