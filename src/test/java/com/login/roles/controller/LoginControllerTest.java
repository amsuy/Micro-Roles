package com.login.roles.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.login.roles.dto.UsuarioDTO;
import com.login.roles.entidad.Usuario;
import com.login.roles.repository.UsuarioRepository;
import com.login.roles.util.JwtUtil;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Usuario usuarioActivo;

    @BeforeEach
    public void setup() {
        // Buscar si ya existe un usuario con username "testuser"
        usuarioRepository.findByUsername("testuser").ifPresent(usuarioRepository::delete);

        usuarioActivo = new Usuario();
        usuarioActivo.setUsername("testuser"); // Usuario de prueba
        usuarioActivo.setPassword(passwordEncoder.encode("test123")); // Contraseña codificada
        usuarioActivo.setRol("ADMINISTRADOR");
        usuarioActivo.setEstado("ACTIVO");
        usuarioActivo.setJwtSecret("jwt_secreto_administrador_123456");
        usuarioActivo.setFechaCreacion(LocalDate.now());
        usuarioActivo.setHoraCreacion(LocalTime.now());

        usuarioRepository.save(usuarioActivo);
    }

    @Test
    public void testLoginExitoso() throws Exception {
        Usuario login = new Usuario();
        login.setUsername("testuser");
        login.setPassword("test123"); // Contraseña en texto plano

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.rol").value("ADMINISTRADOR"))
                .andExpect(jsonPath("$.ruta").value("/admin"));
    }

    @Test
    public void testLoginUsuarioInactivo() throws Exception {
        usuarioActivo.setEstado("INACTIVO");
        usuarioRepository.save(usuarioActivo);

        Usuario login = new Usuario();
        login.setUsername("testuser");
        login.setPassword("test123");

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Acceso denegado: Usuario inactivo"));
    }

    @Test
    public void testRegistrarUsuario() throws Exception {
        // 🔍 Eliminar solo si ya existe "nuevoUser"
        usuarioRepository.findByUsername("nuevoUser").ifPresent(usuarioRepository::delete);

        UsuarioDTO nuevo = new UsuarioDTO("nuevoUser", "clave123", "RECEPCIONISTA", "jwt_secreto_recepcionista_123456");

        mockMvc.perform(post("/api/login/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("✅ Usuario registrado correctamente"));
    }

    @Test
    public void testObtenerUsuariosActivos() throws Exception {
        mockMvc.perform(get("/api/login/usuarios/consultar/activos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].username", hasItem("testuser"))); // Verifica que exista entre los resultados
    }

    @Test
    public void testCambiarPassword() throws Exception {
        mockMvc.perform(put("/api/login/usuarios/" + usuarioActivo.getId() + "/cambiar-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("password", "nueva123"))))
                .andExpect(status().isOk())
                .andExpect(content().string("✅ Contraseña actualizada correctamente"));
    }

    @Test
    public void testDesactivarUsuario() throws Exception {
        mockMvc.perform(put("/api/login/usuarios/" + usuarioActivo.getId() + "/desactivar"))
                .andExpect(status().isOk())
                .andExpect(content().string("✅ Usuario desactivado correctamente"));
    }

    @Test
    public void testRestaurarUsuario() throws Exception {
        usuarioActivo.setEstado("INACTIVO");
        usuarioRepository.save(usuarioActivo);

        mockMvc.perform(put("/api/login/usuarios/" + usuarioActivo.getId() + "/restaurar"))
                .andExpect(status().isOk())
                .andExpect(content().string("✅ Usuario restaurado correctamente"));
    }

    @Test
    public void testObtenerUsuarioActivoPorId() throws Exception {
        mockMvc.perform(get("/api/login/usuarios/consultar/activos/id/" + usuarioActivo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    public void testCambiarRolYJwtSecret() throws Exception {
        // Generar un token válido usando el mismo secreto configurado en setup()
        String token = jwtUtil.generateTokenWithCustomSecret(
                usuarioActivo.getUsername(),
                usuarioActivo.getRol(),
                usuarioActivo.getJwtSecret());

        // JSON con nuevo rol y nuevo JWT secret (debe tener mínimo 32 bytes = 256 bits)
        Map<String, String> cambios = Map.of(
                "rol", "DOCTOR",
                "jwtSecret", "jwt_secreto_doctormedicina_123456" // >= 32 caracteres
        );

        mockMvc.perform(put("/api/login/usuarios/" + usuarioActivo.getId() + "/cambiar-rol-manual")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cambios)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("✅ Rol y JWT Secret actualizados correctamente"))
                .andExpect(jsonPath("$.rol").value("DOCTOR"))
                .andExpect(jsonPath("$.jwtSecret").value("jwt_secreto_doctormedicina_123456"));
    }

}
