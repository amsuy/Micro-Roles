package com.login.roles.security;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.login.roles.entidad.Usuario;
import com.login.roles.repository.UsuarioRepository;
import com.login.roles.util.JwtUtil;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter implements Filter {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();

        // ✅ Rutas públicas permitidas sin JWT
        if (path.equals("/api/login") ||
                path.equals("/api/login/registrar") ||
                path.equals("/api/recepcionista/paciente") ||
                path.equals("/api/recepcionista/pacientes") ||
                path.equals("/api/recepcionista/doctores") ||
                path.equals("/api/recepcionista/cita") ||
                path.equals("/api/recepcionista/citas/programadas") ||
                path.equals("/api/recepcionista/citas/realizadas") ||
                path.equals("/api/recepcionista/citas/canceladas") ||
                path.equals("/api/recepcionista/cita/") ||
                path.startsWith("/api/recepcionista/citas/nit-estado/") ||
                path.equals("/api/admin/pacientes/consultar") ||
                path.startsWith("/api/admin/pacientes/consultar") ||
                path.startsWith("/api/admin/pacientes/ingresar") ||
                path.startsWith("/api/admin/pacientes/buscar/id") ||
                path.startsWith("/api/admin/pacientes/buscar/nit") ||
                path.startsWith("/api/admin/pacientes/buscar/cui") ||
                path.startsWith("/api/admin/pacientes/buscar/direccion") ||
                path.startsWith("/api/admin/pacientes/buscar/telefono") ||
                path.startsWith("/api/admin/pacientes/buscar/fecha") ||
                path.startsWith("/api/admin/pacientes/buscar/nombre") ||
                path.startsWith("/api/admin/pacientes/actualizar/telefono") ||
                path.startsWith("/api/admin/pacientes/actualizar/direccion") ||
                path.startsWith("/api/admin/pacientes/actualizar/nit") ||
                path.startsWith("/api/admin/pacientes/actualizar/cui") ||
                path.startsWith("/api/admin/pacientes/actualizar/edad") ||
                path.startsWith("/api/admin/pacientes/actualizar/nombre") ||
                path.startsWith("/api/admin/pacientes/actualizar") ||
                path.startsWith("/api/admin/pacientes/borrar") ||
                path.startsWith("/api/admin/pacientes/restaurar") ||
                path.startsWith("/api/admin/pacientes/buscar/resumen") ||
                path.equals("/api/recepcionista/citas/paciente-estado") || // ← agrega esta línea
                path.startsWith("/api/recepcionista/citas/paciente-estado/") || // ← para permitir con variables
                path.equals("/api/doctor/citas/estado/PROGRAMADA") ||
                path.equals("/api/doctor/citas/estado/REALIZADA") ||
                path.equals("/api/doctor/citas/estado/CANCELADA") ||
                path.startsWith("/api/doctor/citas/paciente-estado/") ||
                path.startsWith("/api/doctor/citas/fecha/") ||
                path.startsWith("/api/doctor/citas/hoy/idDoctor/") ||
                path.startsWith("/api/doctor/citas/hoy/colegiado/") ||
                path.startsWith("/api/doctor/citas/historial/idDoctor/") ||
                path.startsWith("/api/doctor/citas/historial/colegiado/") ||
                path.startsWith("/api/doctor/citas/rango-fechas/") ||
                path.startsWith("/api/doctor/cita/") && path.endsWith("/realizada") ||
                path.startsWith("/api/doctor/citas/nit-estado/") ||
                path.startsWith("/api/doctor/pacientes/nit/") ||
                path.startsWith("/api/doctor/citas/finalizar/") ||
                path.equals("/api/admin/doctores/consultar") ||
                path.startsWith("/api/admin/doctores/buscar/id/") ||
                path.startsWith("/api/admin/doctores/buscar/nombre") ||
                path.startsWith("/api/admin/doctores/buscar/colegiado") ||
                path.startsWith("/api/admin/doctores/buscar/especialidad") ||
                path.startsWith("/api/admin/doctores/buscar/direccion") ||
                path.startsWith("/api/admin/doctores/buscar/centrohospitalario") ||
                path.startsWith("/api/admin/doctores/buscar/edad") ||
                path.startsWith("/api/admin/doctores/buscar/fecha") ||
                path.equals("/api/admin/doctores/buscar/estado/activo") ||
                path.equals("/api/admin/doctores/buscar/estado/inactivo") ||
                path.startsWith("/api/admin/doctores/actualizar/direccion") ||
                path.startsWith("/api/admin/doctores/actualizar/centrohospitalario") ||
                path.startsWith("/api/admin/doctores/actualizar/especialidad") ||
                path.startsWith("/api/admin/doctores/actualizar/colegiado") ||
                path.startsWith("/api/admin/doctores/actualizar/edad") ||
                path.startsWith("/api/admin/doctores/actualizar/nombre") ||
                path.startsWith("/api/admin/doctores/actualizar/observacion") ||
                path.startsWith("/api/admin/doctores/borrar") ||
                path.startsWith("/api/admin/doctores/restaurar") ||
                path.startsWith("/api/admin/doctores/actualizar") ||
                path.startsWith("/api/admin/doctores/ingresar") ||
                path.startsWith("/api/admin/doctores/buscar/resumen") ||
                path.equals("/api/admin/citas/listar") ||
                path.contains("/api/admin/citas/actualizar/nit") ||
                path.contains("/api/admin/citas/actualizar/colegiado") ||
                path.startsWith("/api/admin/citas/buscar/id/") ||
                path.startsWith("/api/admin/citas/buscar/paciente/") ||
                path.startsWith("/api/admin/citas/buscar/doctor/") ||
                path.startsWith("/api/admin/citas/buscar/estado/") ||
                path.startsWith("/api/admin/citas/buscar/fecha/") ||
                path.startsWith("/api/admin/citas/buscar/rango-fecha/") ||
                path.startsWith("/api/admin/citas/buscar/nit/") ||
                path.startsWith("/api/admin/citas/buscar/colegiado/") ||
                path.startsWith("/api/admin/citas/buscar/motivo/") ||
                path.startsWith("/api/admin/citas/buscar/nombre/") ||
                path.startsWith("/api/admin/citas/buscar/correo/") ||
                path.startsWith("/api/admin/citas/buscar/correo-estado/") ||
                path.equals("/api/admin/citas/listar/realizadas") ||
                path.equals("/api/admin/citas/listar/canceladas") ||
                path.startsWith("/api/admin/citas/ultima-cita/paciente/") ||
                path.startsWith("/api/admin/citas/contar/paciente/") ||
                path.startsWith("/api/admin/citas/contar/doctor/") ||
                path.startsWith("/api/admin/citas/buscar/paciente-estado/") ||
                path.startsWith("/api/admin/citas/hoy/idDoctor/") ||
                path.startsWith("/api/admin/citas/hoy/colegiado/") ||
                path.startsWith("/api/admin/citas/historial/idDoctor/") ||
                path.startsWith("/api/admin/citas/historial/colegiado/") ||
                path.equals("/api/admin/citas/crear") ||
                path.startsWith("/api/admin/citas/actualizar/") ||
                path.startsWith("/api/admin/citas/finalizar/") ||
                path.startsWith("/api/admin/citas/cancelar/") ||
                path.startsWith("/api/admin/usuarios/registrar") ||
                path.equals("/api/login/usuarios/estado/") ||
                path.equals("/api/login/usuarios/consultar/activos") ||
                path.equals("/api/login/usuarios/consultar/inactivos") ||
                path.startsWith("/api/login/usuarios/") && path.contains("cambiar-password") ||
                path.startsWith("/api/login/usuarios/") && path.endsWith("/desactivar") ||
                path.startsWith("/api/login/usuarios/") && path.endsWith("/restaurar") ||
                path.equals("/api/login/usuarios/consultar/activos") ||
                path.startsWith("/api/login/usuarios/consultar/activos/id/") ||
                path.startsWith("/api/login/usuarios/consultar/activos/username/") ||
                path.equals("/api/admin/usuarios/consultar/activos") ||
                path.equals("/api/admin/usuarios/consultar/inactivos") ||
                path.startsWith("/api/admin/usuarios/activos/id/") ||
                path.startsWith("/api/admin/usuarios/activos/username/") ||
                path.startsWith("/api/admin/usuarios/") && path.contains("/cambiar-password") ||
                path.startsWith("/api/admin/usuarios/") && path.contains("/desactivar") ||
                path.startsWith("/api/admin/usuarios/") && path.contains("/restaurar") ||
                path.startsWith("/api/login/usuarios/consultar/inactivos/id/") ||
                path.startsWith("/api/login/usuarios/consultar/inactivos/username/") ||
                path.startsWith("/api/admin/usuarios/inactivos/id/") ||
                path.startsWith("/api/admin/usuarios/inactivos/username/") ||
                path.startsWith("/api/recepcionista/cita/") && path.endsWith("/cancelada") ||
                path.startsWith("/api/admin/pacientes/actualizar/nombre") ||
                path.startsWith("/api/admin/pacientes/actualizar/telefono") ||
                path.startsWith("/api/admin/pacientes/actualizar/direccion") ||
                path.startsWith("/api/admin/pacientes/actualizar/edad") ||
                path.startsWith("/api/admin/pacientes/actualizar/nit") ||
                path.startsWith("/api/admin/pacientes/actualizar/cui") ||
                path.startsWith("/api/admin/citas/actualizar/doctor-colegiado/") ||
                path.startsWith("/api/admin/citas/actualizar/paciente-nit/") ||
                path.startsWith("/api/admin/citas/actualizar/motivo/") ||
                path.startsWith("/api/admin/citas/actualizar/fecha-cita/") ||
                path.startsWith("/api/admin/citas/actualizar/nombre-paciente/") ||
                path.startsWith("/api/admin/citas/actualizar/nombre-doctor/") ||
                path.startsWith("/api/admin/usuarios/") && path.contains("/cambiar-rol") ||
                path.startsWith("/api/login/usuarios/") && path.contains("/cambiar-rol-manual") ||
               path.equals("/api/doctor/consultar") ||
            path.equals("/api/admin/") ||
                path.equals("/api/doctor/pacientes")) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username;

            try {
                // Extraer el username con clave temporal
                username = jwtUtil.extractUsername(token, "claveTemporal");

                Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);

                if (usuarioOpt.isPresent()) {
                    String secret = usuarioOpt.get().getJwtSecret();
                    boolean valid = jwtUtil.validateToken(token, secret);

                    if (valid) {
                        chain.doFilter(request, response); // ✅ Token válido
                        return;
                    }
                }
            } catch (Exception e) {
                // ❌ Error al validar token
            }
        }

        // ❌ Token no enviado o inválido
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
