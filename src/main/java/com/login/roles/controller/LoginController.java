package com.login.roles.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.login.roles.dto.UsuarioDTO;
import com.login.roles.entidad.Usuario;
import com.login.roles.service.UsuarioService;
import com.login.roles.util.JwtUtil;

@RestController
@RequestMapping("/api/login")
@CrossOrigin(origins = "*")
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody Usuario loginData) {
        Optional<Usuario> usuarioOpt = usuarioService.autenticarUsuario(
                loginData.getUsername(),
                loginData.getPassword());

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            if ("ACTIVO".equalsIgnoreCase(usuario.getEstado())) {

                String token = jwtUtil.generateTokenWithCustomSecret(
                        usuario.getUsername(),
                        usuario.getRol(),
                        usuario.getJwtSecret());

                String ruta = switch (usuario.getRol().toUpperCase()) {
                    case "RECEPCIONISTA" -> "/recepcionista";
                    case "DOCTOR" -> "/doctor";
                    case "ADMINISTRADOR" -> "/admin";
                    default -> "/";
                };

                return ResponseEntity.ok(Map.of(
                        "token", token,
                        "username", usuario.getUsername(),
                        "rol", usuario.getRol(),
                        "ruta", ruta));
            } else {
                return ResponseEntity.status(403).body("Acceso denegado: Usuario inactivo");
            }
        }

        return ResponseEntity.status(401).body("Usuario o contraseña incorrectos");
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody UsuarioDTO dto) {
        Usuario nuevo = usuarioService.registrarUsuario(dto);
        return ResponseEntity.ok(Map.of(
                "mensaje", "✅ Usuario registrado correctamente",
                "username", nuevo.getUsername(),
                "rol", nuevo.getRol()));
    }

    @GetMapping("/usuarios/consultar/activos")
    public ResponseEntity<?> obtenerUsuariosActivos() {
        return ResponseEntity.ok(usuarioService.buscarPorEstado("ACTIVO"));
    }

    @GetMapping("/usuarios/consultar/inactivos")
    public ResponseEntity<?> obtenerUsuariosInactivos() {
        return ResponseEntity.ok(usuarioService.buscarPorEstado("INACTIVO"));
    }

    @PutMapping("/usuarios/{id}/cambiar-password")
    public ResponseEntity<?> cambiarPassword(
            @PathVariable("id") Long id,
            @RequestBody Map<String, String> datos) {

        String nuevaPassword = datos.get("password");

        return usuarioService.actualizarPassword(id, nuevaPassword)
                .map(usuario -> ResponseEntity.ok("✅ Contraseña actualizada correctamente"))
                .orElse(ResponseEntity.status(404).body("❌ Usuario no encontrado"));
    }

    // 🔴 Borrar lógicamente por ID
    @PutMapping("/usuarios/{id}/desactivar")
    public ResponseEntity<?> desactivarUsuario(@PathVariable("id") Long id) {
        boolean eliminado = usuarioService.desactivarUsuario(id);
        if (eliminado) {
            return ResponseEntity.ok("✅ Usuario desactivado correctamente");
        } else {
            return ResponseEntity.status(404).body("❌ Usuario no encontrado");
        }
    }

    // 🟢 Restaurar usuario por ID
    @PutMapping("/usuarios/{id}/restaurar")
    public ResponseEntity<?> restaurarUsuario(@PathVariable("id") Long id) {
        boolean restaurado = usuarioService.restaurarUsuario(id);
        if (restaurado) {
            return ResponseEntity.ok("✅ Usuario restaurado correctamente");
        } else {
            return ResponseEntity.status(404).body("❌ Usuario no encontrado");
        }
    }

    @GetMapping("/usuarios/consultar/activos/id/{id}")
    public ResponseEntity<?> obtenerUsuarioActivoPorId(@PathVariable("id") Long id) {
        Optional<Usuario> usuario = usuarioService.buscarActivoPorId(id);
        return usuario.isPresent()
                ? ResponseEntity.ok(usuario.get())
                : ResponseEntity.status(404).body("❌ Usuario activo no encontrado con ese ID");
    }

    @GetMapping("/usuarios/consultar/activos/username/{username}")
    public ResponseEntity<?> obtenerUsuarioActivoPorUsername(@PathVariable("username") String username) {
        Optional<Usuario> usuario = usuarioService.buscarActivoPorUsername(username);
        return usuario.isPresent()
                ? ResponseEntity.ok(usuario.get())
                : ResponseEntity.status(404).body("❌ Usuario activo no encontrado con ese username");
    }

    @GetMapping("/usuarios/consultar/inactivos/id/{id}")
    public ResponseEntity<?> obtenerUsuarioInactivoPorId(@PathVariable("id") Long id) {
        Optional<Usuario> usuario = usuarioService.buscarInactivoPorId(id);
        return usuario.isPresent()
                ? ResponseEntity.ok(usuario.get())
                : ResponseEntity.status(404).body("❌ Usuario inactivo no encontrado con ese ID");
    }

    @GetMapping("/usuarios/consultar/inactivos/username/{username}")
    public ResponseEntity<?> obtenerUsuarioInactivoPorUsername(@PathVariable("username") String username) {
        Optional<Usuario> usuario = usuarioService.buscarInactivoPorUsername(username);
        return usuario.isPresent()
                ? ResponseEntity.ok(usuario.get())
                : ResponseEntity.status(404).body("❌ Usuario inactivo no encontrado con ese username");
    }

    @PutMapping("/usuarios/{id}/cambiar-rol-manual")
    public ResponseEntity<?> cambiarRolManual(@PathVariable("id") Long id, @RequestBody Map<String, String> datos) {
        String nuevoRol = datos.get("rol");
        String nuevoJwtSecret = datos.get("jwtSecret");

        if (nuevoRol == null || nuevoJwtSecret == null) {
            return ResponseEntity.badRequest().body("⚠️ El rol y el JWT Secret son requeridos");
        }

        return usuarioService.cambiarRolYJwtSecret(id, nuevoRol, nuevoJwtSecret)
                .map(usuario -> ResponseEntity.ok(Map.of(
                        "mensaje", "✅ Rol y JWT Secret actualizados correctamente",
                        "username", usuario.getUsername(),
                        "rol", usuario.getRol(),
                        "jwtSecret", usuario.getJwtSecret())))
                .orElse(ResponseEntity.status(404).body(Map.of("mensaje", "❌ Usuario no encontrado")));

    }

}
