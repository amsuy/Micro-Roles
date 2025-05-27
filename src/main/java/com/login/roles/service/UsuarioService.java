package com.login.roles.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.login.roles.dto.UsuarioDTO;
import com.login.roles.entidad.Usuario;
import com.login.roles.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    // ✅ Codificador para encriptar contraseñas
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ✅ Registrar usuario con contraseña encriptada
    public Usuario registrarUsuario(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword())); // Encriptar aquí
        usuario.setRol(dto.getRol());
        usuario.setJwtSecret(dto.getJwtSecret());

        // La BD también puede generar esto, pero por si acaso:
        usuario.setEstado("ACTIVO");
        usuario.setFechaCreacion(LocalDate.now());
        usuario.setHoraCreacion(LocalTime.now());

        return usuarioRepository.save(usuario);
    }

    // ✅ Autenticar usuario (login)
    public Optional<Usuario> autenticarUsuario(String username, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (passwordEncoder.matches(password, usuario.getPassword())) {
                return Optional.of(usuario);
            }
        }
        return Optional.empty();
    }

    // ✅ Consultas
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public List<Usuario> buscarPorRol(String rol) {
        return usuarioRepository.findByRol(rol);
    }

    public List<Usuario> buscarPorEstado(String estado) {
        return usuarioRepository.findByEstado(estado);
    }

    public List<Usuario> buscarPorFecha(LocalDate fecha) {
        return usuarioRepository.findByFechaCreacion(fecha);
    }

    public List<Usuario> buscarPorHora(LocalTime hora) {
        return usuarioRepository.findByHoraCreacion(hora);
    }

    // ✅ Actualizar todos los campos editables
    public Optional<Usuario> actualizarUsuario(Long id, UsuarioDTO dto) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setUsername(dto.getUsername());
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
            usuario.setRol(dto.getRol());
            return usuarioRepository.save(usuario);
        });
    }

    // ✅ Borrar lógicamente un usuario
    public boolean desactivarUsuario(Long id) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setEstado("INACTIVO");
            usuarioRepository.save(usuario);
            return true;
        }).orElse(false);
    }

    // ✅ Restaurar un usuario
    public boolean restaurarUsuario(Long id) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setEstado("ACTIVO");
            usuarioRepository.save(usuario);
            return true;
        }).orElse(false);
    }

    // ✅ Listar usuarios inactivos
    public List<Usuario> listarInactivos() {
        return usuarioRepository.findByEstado("INACTIVO");
    }

    // ✅ Actualizar solo username
    public Optional<Usuario> actualizarUsername(Long id, String nuevoUsername) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setUsername(nuevoUsername);
            return usuarioRepository.save(usuario);
        });
    }

    // ✅ Actualizar solo password
    public Optional<Usuario> actualizarPassword(Long id, String nuevaPassword) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setPassword(passwordEncoder.encode(nuevaPassword));
            return usuarioRepository.save(usuario);
        });
    }

    // ✅ Actualizar solo rol
    public Optional<Usuario> actualizarRol(Long id, String nuevoRol) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setRol(nuevoRol);
            return usuarioRepository.save(usuario);
        });
    }

    public Optional<Usuario> buscarActivoPorId(Long id) {
        return usuarioRepository.findByIdAndEstado(id, "ACTIVO");
    }

    public Optional<Usuario> buscarActivoPorUsername(String username) {
        return usuarioRepository.findByUsernameAndEstado(username, "ACTIVO");
    }

    public Optional<Usuario> buscarInactivoPorId(Long id) {
        return usuarioRepository.findByIdAndEstado(id, "INACTIVO");
    }

    public Optional<Usuario> buscarInactivoPorUsername(String username) {
        return usuarioRepository.findByUsernameAndEstado(username, "INACTIVO");
    }

    public Optional<Usuario> cambiarRolYJwtSecret(Long id, String nuevoRol, String nuevoJwtSecret) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setRol(nuevoRol);
            usuario.setJwtSecret(nuevoJwtSecret);
            return usuarioRepository.save(usuario);
        });
    }

}
