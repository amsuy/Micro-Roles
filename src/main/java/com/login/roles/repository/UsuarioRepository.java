package com.login.roles.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.login.roles.entidad.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Buscar por username
    Optional<Usuario> findByUsername(String username);

    // Buscar por rol
    List<Usuario> findByRol(String rol);

    // Buscar por fecha de creación
    List<Usuario> findByFechaCreacion(LocalDate fechaCreacion);

    // Buscar por hora de creación
    List<Usuario> findByHoraCreacion(LocalTime horaCreacion);

    // Buscar por estado
    List<Usuario> findByEstado(String estado);

    Optional<Usuario> findByIdAndEstado(Long id, String estado);

    Optional<Usuario> findByUsernameAndEstado(String username, String estado);

}
