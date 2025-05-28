package com.login.roles.controller;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.login.roles.dto.CitaProgramadaDTO;
import com.login.roles.dto.DoctorDTO;
import com.login.roles.dto.PacienteDTO;
import com.login.roles.dto.UsuarioDTO;
import com.login.roles.entidad.Usuario;
import com.login.roles.service.UsuarioService;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdministradorController {

    @Autowired
    private RestTemplate restTemplate;

   @Value("${paciente.service.url}")
private String BASE_URL;

@Value("${doctor.service.url}")
private String DOCTOR_URL;

@Value("${citas.service.url}")
private String URL_CITAS;


    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/pacientes/consultar")
    public ResponseEntity<?> consultarPacientes() {
        ResponseEntity<PacienteDTO[]> response = restTemplate.getForEntity(BASE_URL + "/consultar",
                PacienteDTO[].class);
        return ResponseEntity.ok(Arrays.asList(response.getBody()));
    }

    @PostMapping("/pacientes/ingresar")
    public ResponseEntity<?> ingresar(@RequestBody PacienteDTO pacienteDTO) {
        HttpEntity<PacienteDTO> entity = new HttpEntity<>(pacienteDTO, buildHeaders());
        return restTemplate.exchange(BASE_URL + "/ingresar", HttpMethod.POST, entity, String.class);
    }

    @GetMapping("/pacientes/buscar/id/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable("id") Long id) {
        return restTemplate.exchange(BASE_URL + "/buscar/id/" + id, HttpMethod.GET, null, String.class);
    }

    @GetMapping("/pacientes/buscar/nit/{nit}")
    public ResponseEntity<?> buscarPorNit(@PathVariable("nit") Long nit) {
        return restTemplate.exchange(BASE_URL + "/buscar/nit/" + nit, HttpMethod.GET, null, String.class);
    }

    @GetMapping("/pacientes/buscar/cui/{cui}")
    public ResponseEntity<?> buscarPorCui(@PathVariable("cui") Long cui) {
        return restTemplate.exchange(BASE_URL + "/buscar/cui/" + cui, HttpMethod.GET, null, String.class);
    }

    @GetMapping("/pacientes/buscar/direccion/{direccion}")
    public ResponseEntity<?> buscarPorDireccion(@PathVariable("direccion") String direccion) {
        return restTemplate.exchange(BASE_URL + "/buscar/direccion/" + direccion, HttpMethod.GET, null, String.class);
    }

    @GetMapping("/pacientes/buscar/telefono/{telefono}")
    public ResponseEntity<?> buscarPorTelefono(@PathVariable("telefono") Integer telefono) {
        return restTemplate.exchange(BASE_URL + "/buscar/telefono/" + telefono, HttpMethod.GET, null, String.class);
    }

    @GetMapping("/pacientes/buscar/fecha/{fecha}")
    public ResponseEntity<?> buscarPorFecha(@PathVariable("fecha") String fecha) {
        return restTemplate.exchange(BASE_URL + "/buscar/fecha/" + fecha, HttpMethod.GET, null, String.class);
    }

    @GetMapping("/pacientes/buscar/nombre/{nombre}")
    public ResponseEntity<?> buscarPorNombre(@PathVariable("nombre") String nombre) {
        return restTemplate.exchange(BASE_URL + "/buscar/nombre/" + nombre, HttpMethod.GET, null, String.class);
    }

    @PutMapping("/pacientes/actualizar/telefono/{id}/{telefono}")
    public ResponseEntity<?> actualizarTelefono(@PathVariable("id") Long id,
            @PathVariable("telefono") Integer telefono) {
        return restTemplate.exchange(BASE_URL + "/actualizar/telefono/" + id + "/" + telefono, HttpMethod.PUT, null,
                String.class);
    }

    @PutMapping("/pacientes/actualizar/direccion/{id}/{direccion}")
    public ResponseEntity<?> actualizarDireccion(@PathVariable("id") Long id,
            @PathVariable("direccion") String direccion) {
        return restTemplate.exchange(BASE_URL + "/actualizar/direccion/" + id + "/" + direccion, HttpMethod.PUT, null,
                String.class);
    }

    @PutMapping("/pacientes/actualizar/nit/{id}/{nit}")
    public ResponseEntity<?> actualizarNit(@PathVariable("id") Long id, @PathVariable("nit") int nit) {
        return restTemplate.exchange(BASE_URL + "/actualizar/nit/" + id + "/" + nit, HttpMethod.PUT, null,
                String.class);
    }

    @PutMapping("/pacientes/actualizar/cui/{id}/{cui}")
    public ResponseEntity<?> actualizarCui(@PathVariable("id") Long id, @PathVariable("cui") int cui) {
        return restTemplate.exchange(BASE_URL + "/actualizar/cui/" + id + "/" + cui, HttpMethod.PUT, null,
                String.class);
    }

    @PutMapping("/pacientes/actualizar/edad/{id}/{edad}")
    public ResponseEntity<?> actualizarEdad(@PathVariable("id") Long id, @PathVariable("edad") int edad) {
        return restTemplate.exchange(BASE_URL + "/actualizar/edad/" + id + "/" + edad, HttpMethod.PUT, null,
                String.class);
    }

    @PutMapping("/pacientes/actualizar/nombre/{id}/{nombre}")
    public ResponseEntity<?> actualizarNombre(@PathVariable("id") Long id, @PathVariable("nombre") String nombre) {
        return restTemplate.exchange(BASE_URL + "/actualizar/nombre/" + id + "/" + nombre, HttpMethod.PUT, null,
                String.class);
    }

    @PutMapping("/pacientes/borrar/{id}")
    public ResponseEntity<?> borrarPaciente(@PathVariable("id") Long id) {
        return restTemplate.exchange(BASE_URL + "/borrar/" + id, HttpMethod.PUT, null, String.class);
    }

    @PutMapping("/pacientes/restaurar/{id}")
    public ResponseEntity<?> restaurarPaciente(@PathVariable("id") Long id) {
        return restTemplate.exchange(BASE_URL + "/restaurar/" + id, HttpMethod.PUT, null, String.class);
    }

    @PutMapping("/pacientes/actualizar/{id}")
    public ResponseEntity<?> actualizarPaciente(@PathVariable("id") Long id, @RequestBody PacienteDTO pacienteDTO) {
        HttpEntity<PacienteDTO> entity = new HttpEntity<>(pacienteDTO, buildHeaders());
        return restTemplate.exchange(BASE_URL + "/actualizar/" + id, HttpMethod.PUT, entity, String.class);
    }

    @GetMapping("/pacientes/buscar/resumen/{id}")
    public ResponseEntity<?> resumenPaciente(@PathVariable("id") Long id) {
        return restTemplate.exchange(BASE_URL + "/buscar/resumen/" + id, HttpMethod.GET, null, String.class);
    }

    @GetMapping("/doctores/consultar")
public ResponseEntity<?> listarDoctores() {
    ResponseEntity<DoctorDTO[]> response = restTemplate.getForEntity(DOCTOR_URL + "/consultar",
            DoctorDTO[].class);
    return ResponseEntity.ok(Arrays.asList(response.getBody()));
}


    @GetMapping("/doctores/buscar/id/{id}")
    public ResponseEntity<?> buscarDoctorPorId(@PathVariable("id") Long id) {
        String url = DOCTOR_URL + "/buscar/id/{id}";
        return restTemplate.exchange(url, HttpMethod.GET, null, String.class, Map.of("id", id));
    }

    @GetMapping("/doctores/buscar/nombre/{nombre}")
    public ResponseEntity<?> buscarDoctorPorNombre(@PathVariable("nombre") String nombre) {
        String url = DOCTOR_URL + "/buscar/nombre/{nombre}";
        return restTemplate.exchange(url, HttpMethod.GET, null, String.class, Map.of("nombre", nombre));
    }

    @GetMapping("/doctores/buscar/colegiado/{colegiado}")
    public ResponseEntity<?> buscarDoctorPorColegiado(@PathVariable("colegiado") String colegiado) {
        String url = DOCTOR_URL + "/buscar/colegiado/{colegiado}";
        return restTemplate.exchange(url, HttpMethod.GET, null, String.class, Map.of("colegiado", colegiado));
    }

    @GetMapping("/doctores/buscar/especialidad/{especialidad}")
    public ResponseEntity<?> buscarDoctorPorEspecialidad(@PathVariable("especialidad") String especialidad) {
        String url = DOCTOR_URL + "/buscar/especialidad/{especialidad}";
        return restTemplate.exchange(url, HttpMethod.GET, null, String.class, Map.of("especialidad", especialidad));
    }

    @GetMapping("/doctores/buscar/direccion/{direccion}")
    public ResponseEntity<?> buscarDoctorPorDireccion(@PathVariable("direccion") String direccion) {
        String url = DOCTOR_URL + "/buscar/direccion/{direccion}";
        return restTemplate.exchange(url, HttpMethod.GET, null, String.class, Map.of("direccion", direccion));
    }

    @GetMapping("/doctores/buscar/centrohospitalario/{centro}")
    public ResponseEntity<?> buscarDoctorPorCentro(@PathVariable("centro") String centro) {
        String url = DOCTOR_URL + "/buscar/centrohospitalario/{centro}";
        return restTemplate.exchange(url, HttpMethod.GET, null, String.class, Map.of("centro", centro));
    }

    @GetMapping("/doctores/buscar/edad/{edad}")
    public ResponseEntity<?> buscarDoctorPorEdad(@PathVariable("edad") int edad) {
        String url = DOCTOR_URL + "/buscar/edad/{edad}";
        return restTemplate.exchange(url, HttpMethod.GET, null, String.class, Map.of("edad", edad));
    }

    @GetMapping("/doctores/buscar/fecha/{fecha}")
    public ResponseEntity<?> buscarDoctorPorFecha(@PathVariable("fecha") String fecha) {
        String url = DOCTOR_URL + "/buscar/fecha/{fecha}";
        return restTemplate.exchange(url, HttpMethod.GET, null, String.class, Map.of("fecha", fecha));
    }

    @GetMapping("/doctores/buscar/estado/activo")
    public ResponseEntity<?> doctoresActivos() {
        return restTemplate.exchange(DOCTOR_URL + "/buscar/estado/activo", HttpMethod.GET, null, String.class);
    }

    @GetMapping("/doctores/buscar/estado/inactivo")
    public ResponseEntity<?> doctoresInactivos() {
        return restTemplate.exchange(DOCTOR_URL + "/buscar/estado/inactivo", HttpMethod.GET, null, String.class);
    }

    @GetMapping("/doctores/buscar/resumen/{id}")
    public ResponseEntity<?> resumenDoctor(@PathVariable("id") Long id) {
        String url = DOCTOR_URL + "/buscar/resumen/{id}";
        return restTemplate.exchange(url, HttpMethod.GET, null, String.class, Map.of("id", id));
    }

    @PostMapping("/doctores/ingresar")
    public ResponseEntity<?> registrarDoctor(@RequestBody DoctorDTO doctorDTO) {
        HttpEntity<DoctorDTO> entity = new HttpEntity<>(doctorDTO, buildHeaders());
        return restTemplate.exchange(DOCTOR_URL + "/ingresar", HttpMethod.POST, entity, String.class);
    }

    @PutMapping("/doctores/actualizar/direccion/{id}/{direccion}")
    public ResponseEntity<?> actualizarDireccionDoctor(@PathVariable("id") Long id,
            @PathVariable("direccion") String direccion) {
        return restTemplate.exchange(DOCTOR_URL + "/actualizar/direccion/" + id + "/" + direccion, HttpMethod.PUT, null,
                String.class);
    }

    @PutMapping("/doctores/actualizar/centrohospitalario/{id}/{centro}")
    public ResponseEntity<?> actualizarCentroHospitalario(@PathVariable("id") Long id,
            @PathVariable("centro") String centro) {
        return restTemplate.exchange(DOCTOR_URL + "/actualizar/centrohospitalario/" + id + "/" + centro, HttpMethod.PUT,
                null, String.class);
    }

    @PutMapping("/doctores/actualizar/especialidad/{id}/{especialidad}")
    public ResponseEntity<?> actualizarEspecialidad(@PathVariable("id") Long id,
            @PathVariable("especialidad") String especialidad) {
        return restTemplate.exchange(DOCTOR_URL + "/actualizar/especialidad/" + id + "/" + especialidad, HttpMethod.PUT,
                null, String.class);
    }

    @PutMapping("/doctores/actualizar/colegiado/{id}/{colegiado}")
    public ResponseEntity<?> actualizarColegiado(@PathVariable("id") Long id,
            @PathVariable("colegiado") String colegiado) {
        return restTemplate.exchange(DOCTOR_URL + "/actualizar/colegiado/" + id + "/" + colegiado, HttpMethod.PUT, null,
                String.class);
    }

    @PutMapping("/doctores/actualizar/edad/{id}/{edad}")
    public ResponseEntity<?> actualizarEdadDoctor(@PathVariable("id") Long id, @PathVariable("edad") int edad) {
        return restTemplate.exchange(DOCTOR_URL + "/actualizar/edad/" + id + "/" + edad, HttpMethod.PUT, null,
                String.class);
    }

    @PutMapping("/doctores/actualizar/nombre/{id}/{nombre}")
    public ResponseEntity<?> actualizarNombreDoctor(@PathVariable("id") Long id,
            @PathVariable("nombre") String nombre) {
        return restTemplate.exchange(DOCTOR_URL + "/actualizar/nombre/" + id + "/" + nombre, HttpMethod.PUT, null,
                String.class);
    }

    @PutMapping("/doctores/actualizar/observacion/{id}/{observacion}")
    public ResponseEntity<?> actualizarObservacionDoctor(@PathVariable("id") Long id,
            @PathVariable("observacion") String observacion) {
        return restTemplate.exchange(DOCTOR_URL + "/actualizar/observacion/" + id + "/" + observacion, HttpMethod.PUT,
                null, String.class);
    }

    @PutMapping("/doctores/borrar/{id}")
    public ResponseEntity<?> borrarDoctor(@PathVariable("id") Long id) {
        return restTemplate.exchange(DOCTOR_URL + "/borrar/" + id, HttpMethod.PUT, null, String.class);
    }

    @PutMapping("/doctores/restaurar/{id}")
    public ResponseEntity<?> restaurarDoctor(@PathVariable("id") Long id) {
        return restTemplate.exchange(DOCTOR_URL + "/restaurar/" + id, HttpMethod.PUT, null, String.class);
    }

    @PutMapping("/doctores/actualizar/{id}")
    public ResponseEntity<?> actualizarDoctorCompleto(@PathVariable("id") Long id, @RequestBody DoctorDTO doctorDTO) {
        HttpEntity<DoctorDTO> entity = new HttpEntity<>(doctorDTO, buildHeaders());
        return restTemplate.exchange(DOCTOR_URL + "/actualizar/" + id, HttpMethod.PUT, entity, String.class);
    }

    // Listar todas las citas
    @GetMapping("/citas/listar")
    public ResponseEntity<?> listarCitas() {
        return restTemplate.getForEntity(URL_CITAS + "/listar", String.class);
    }

    // Buscar cita por ID
    @GetMapping("/citas/buscar/id/{id}")
    public ResponseEntity<?> obtenerCitaPorId(@PathVariable("id") Integer id) {
        return restTemplate.getForEntity(URL_CITAS + "/buscar/id/" + id, String.class);
    }

    // Buscar citas por estado
    @GetMapping("/citas/buscar/estado/{estado}")
    public ResponseEntity<?> obtenerCitasPorEstado(@PathVariable("estado") String estado) {
        return restTemplate.getForEntity(URL_CITAS + "/buscar/estado/" + estado, String.class);
    }

    // Buscar por fecha de inicio y fin
    @GetMapping("/citas/buscar/fecha/{fechaInicio}/{fechaFin}")
    public ResponseEntity<?> obtenerCitasPorFecha(
            @PathVariable("fechaInicio") String fechaInicio,
            @PathVariable("fechaFin") String fechaFin) {
        return restTemplate.getForEntity(URL_CITAS + "/buscar/fecha/" + fechaInicio + "/" + fechaFin, String.class);
    }

    // Buscar por rango de fecha
    @GetMapping("/citas/buscar/rango-fecha/{fechaInicio}/{fechaFin}")
    public ResponseEntity<?> buscarCitasPorRangoDeFecha(
            @PathVariable("fechaInicio") String fechaInicio,
            @PathVariable("fechaFin") String fechaFin) {
        return restTemplate.getForEntity(URL_CITAS + "/buscar/rango-fecha/" + fechaInicio + "/" + fechaFin,
                String.class);
    }

    // Buscar por NIT
    @GetMapping("/citas/buscar/nit/{nit}")
    public ResponseEntity<?> obtenerCitasPorNit(@PathVariable("nit") Long nit) {
        return restTemplate.getForEntity(URL_CITAS + "/buscar/nit/" + nit, String.class);
    }

    // Buscar por colegiado
    @GetMapping("/citas/buscar/colegiado/{colegiado}")
    public ResponseEntity<?> obtenerCitasPorColegiado(@PathVariable("colegiado") String colegiado) {
        return restTemplate.getForEntity(URL_CITAS + "/buscar/colegiado/" + colegiado, String.class);
    }

    // Buscar por motivo
    @GetMapping("/citas/buscar/motivo/{motivo}")
    public ResponseEntity<?> obtenerCitasPorMotivo(@PathVariable("motivo") String motivo) {
        return restTemplate.getForEntity(URL_CITAS + "/buscar/motivo/" + motivo, String.class);
    }

    // Buscar por nombre
    @GetMapping("/citas/buscar/nombre/{nombre}")
    public ResponseEntity<?> obtenerCitasPorNombre(@PathVariable("nombre") String nombre) {
        return restTemplate.getForEntity(URL_CITAS + "/buscar/nombre/" + nombre, String.class);
    }

    // Buscar por correo
    @GetMapping("/citas/buscar/correo/{correo}")
    public ResponseEntity<?> obtenerCitasPorCorreo(@PathVariable("correo") String correo) {
        return restTemplate.getForEntity(URL_CITAS + "/buscar/correo/" + correo, String.class);
    }

    // Buscar por correo y estado
    @GetMapping("/citas/buscar/correo-estado/{correo}/{estado}")
    public ResponseEntity<?> obtenerCitasPorCorreoYEstado(
            @PathVariable("correo") String correo,
            @PathVariable("estado") String estado) {
        return restTemplate.getForEntity(URL_CITAS + "/buscar/correo-estado/" + correo + "/" + estado, String.class);
    }

    // Listar realizadas
    @GetMapping("/citas/listar/realizadas")
    public ResponseEntity<?> listarCitasRealizadas() {
        return restTemplate.getForEntity(URL_CITAS + "/listar/realizadas", String.class);
    }

    // Listar canceladas
    @GetMapping("/citas/listar/canceladas")
    public ResponseEntity<?> listarCitasCanceladas() {
        return restTemplate.getForEntity(URL_CITAS + "/listar/canceladas", String.class);
    }

    // Última cita de paciente
    @GetMapping("/citas/ultima-cita/paciente/{idPaciente}")
    public ResponseEntity<?> obtenerUltimaCitaPorPaciente(@PathVariable("idPaciente") Long idPaciente) {
        return restTemplate.getForEntity(URL_CITAS + "/ultima-cita/paciente/" + idPaciente, String.class);
    }

    // Buscar por fecha única
    @GetMapping("/citas/buscar/fecha/{fecha}")
    public ResponseEntity<?> obtenerCitasPorFechaUnica(@PathVariable("fecha") String fecha) {
        return restTemplate.getForEntity(URL_CITAS + "/buscar/fecha/" + fecha, String.class);
    }

    // Citas de hoy por ID doctor
    @GetMapping("/citas/hoy/idDoctor/{idDoctor}")
    public ResponseEntity<?> citasDeHoyPorIdDoctor(@PathVariable("idDoctor") Long idDoctor) {
        return restTemplate.getForEntity(URL_CITAS + "/hoy/idDoctor/" + idDoctor, String.class);
    }

    // Citas de hoy por colegiado
    @GetMapping("/citas/hoy/colegiado/{colegiado}")
    public ResponseEntity<?> citasDeHoyPorColegiado(@PathVariable("colegiado") String colegiado) {
        return restTemplate.getForEntity(URL_CITAS + "/hoy/colegiado/" + colegiado, String.class);
    }

    // Historial por colegiado
    @GetMapping("/citas/historial/colegiado/{colegiado}/{inicio}/{fin}")
    public ResponseEntity<?> historialCitasPorColegiado(
            @PathVariable("colegiado") String colegiado,
            @PathVariable("inicio") String inicio,
            @PathVariable("fin") String fin) {
        return restTemplate.getForEntity(URL_CITAS + "/historial/colegiado/" + colegiado + "/" + inicio + "/" + fin,
                String.class);
    }

    // Crear una nueva cita
    @PostMapping("/citas/crear")
public ResponseEntity<?> crearCita(@RequestBody CitaProgramadaDTO citaDTO) {
    try {
        HttpEntity<CitaProgramadaDTO> request = new HttpEntity<>(citaDTO, buildHeaders());
        return restTemplate.postForEntity(URL_CITAS + "/crear", request, String.class);
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Error al crear cita: " + e.getMessage());
    }
}


    // Actualizar una cita
   @PutMapping("/citas/actualizar/{id}")
public ResponseEntity<?> actualizarCita(@PathVariable("id") Integer id, @RequestBody CitaProgramadaDTO citaDTO) {
    try {
        HttpEntity<CitaProgramadaDTO> request = new HttpEntity<>(citaDTO, buildHeaders());
        return restTemplate.exchange(
                URL_CITAS + "/actualizar/" + id,
                HttpMethod.PUT,
                request,
                String.class);
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Error al actualizar cita: " + e.getMessage());
    }
}


    // Finalizar una cita (cambia estado y registra costo y fechaFin)
   @PutMapping("/citas/finalizar/{id}")
public ResponseEntity<?> finalizarCita(@PathVariable("id") Integer id, @RequestBody CitaProgramadaDTO citaDTO) {
    try {
        HttpEntity<CitaProgramadaDTO> request = new HttpEntity<>(citaDTO, buildHeaders());
        return restTemplate.exchange(
                URL_CITAS + "/finalizar/" + id,
                HttpMethod.PUT,
                request,
                String.class);
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Error al finalizar cita: " + e.getMessage());
    }
}

    // Cancelar una cita (requiere solo fechaFin en el DTO)
    @PutMapping("/citas/cancelar/{id}")
public ResponseEntity<?> cancelarCita(@PathVariable("id") Integer id, @RequestBody CitaProgramadaDTO citaDTO) {
    try {
        HttpEntity<CitaProgramadaDTO> request = new HttpEntity<>(citaDTO, buildHeaders());
        return restTemplate.exchange(
                URL_CITAS + "/cancelar/" + id,
                HttpMethod.PUT,
                request,
                String.class);
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Error al cancelar cita: " + e.getMessage());
    }
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

    @PutMapping("/usuarios/{id}/desactivar")
    public ResponseEntity<?> desactivarUsuario(@PathVariable("id") Long id) {
        boolean eliminado = usuarioService.desactivarUsuario(id);
        if (eliminado) {
            return ResponseEntity.ok("✅ Usuario desactivado correctamente");
        } else {
            return ResponseEntity.status(404).body("❌ Usuario no encontrado");
        }
    }

    @PutMapping("/usuarios/{id}/restaurar")
    public ResponseEntity<?> restaurarUsuario(@PathVariable("id") Long id) {
        boolean restaurado = usuarioService.restaurarUsuario(id);
        if (restaurado) {
            return ResponseEntity.ok("✅ Usuario restaurado correctamente");
        } else {
            return ResponseEntity.status(404).body("❌ Usuario no encontrado");
        }
    }

    @GetMapping("/usuarios/activos/id/{id}")
    public ResponseEntity<?> obtenerUsuarioActivoPorId(@PathVariable("id") Long id) {
        Optional<Usuario> usuario = usuarioService.buscarActivoPorId(id);
        return usuario.isPresent()
                ? ResponseEntity.ok(usuario.get())
                : ResponseEntity.status(404).body("❌ Usuario activo no encontrado con ese ID");
    }

    @GetMapping("/usuarios/activos/username/{username}")
    public ResponseEntity<?> obtenerUsuarioActivoPorUsername(@PathVariable("username") String username) {
        Optional<Usuario> usuario = usuarioService.buscarActivoPorUsername(username);
        return usuario.isPresent()
                ? ResponseEntity.ok(usuario.get())
                : ResponseEntity.status(404).body("❌ Usuario activo no encontrado con ese username");
    }

    @GetMapping("/usuarios/inactivos/id/{id}")
    public ResponseEntity<?> obtenerUsuarioInactivoPorId(@PathVariable("id") Long id) {
        Optional<Usuario> usuario = usuarioService.buscarInactivoPorId(id);
        return usuario.isPresent()
                ? ResponseEntity.ok(usuario.get())
                : ResponseEntity.status(404).body("❌ Usuario inactivo no encontrado con ese ID");
    }

    @GetMapping("/usuarios/inactivos/username/{username}")
    public ResponseEntity<?> obtenerUsuarioInactivoPorUsername(@PathVariable("username") String username) {
        Optional<Usuario> usuario = usuarioService.buscarInactivoPorUsername(username);
        return usuario.isPresent()
                ? ResponseEntity.ok(usuario.get())
                : ResponseEntity.status(404).body("❌ Usuario inactivo no encontrado con ese username");
    }

    @PutMapping("/citas/actualizar/motivo/{id}")
    public ResponseEntity<?> actualizarMotivoConsulta(
            @PathVariable("id") Integer id,
            @RequestBody CitaProgramadaDTO dto) {
        HttpEntity<CitaProgramadaDTO> request = new HttpEntity<>(dto, buildHeaders());
        return restTemplate.exchange(
                URL_CITAS + "/actualizar/motivo/" + id,
                HttpMethod.PUT,
                request,
                String.class);
    }

    @PutMapping("/citas/actualizar/fecha-cita/{id}")
    public ResponseEntity<?> actualizarFechaCita(
            @PathVariable("id") Integer id,
            @RequestBody CitaProgramadaDTO dto) {
        HttpEntity<CitaProgramadaDTO> request = new HttpEntity<>(dto, buildHeaders());
        return restTemplate.exchange(
                URL_CITAS + "/actualizar/fecha-cita/" + id,
                HttpMethod.PUT,
                request,
                String.class);
    }

    @PutMapping("/citas/actualizar/nombre-paciente/{id}")
    public ResponseEntity<?> actualizarNombrePaciente(
            @PathVariable("id") Integer id,
            @RequestBody CitaProgramadaDTO dto) {
        HttpEntity<CitaProgramadaDTO> request = new HttpEntity<>(dto, buildHeaders());
        return restTemplate.exchange(
                URL_CITAS + "/actualizar/nombre-paciente/" + id,
                HttpMethod.PUT,
                request,
                String.class);
    }

    @PutMapping("/citas/actualizar/nombre-doctor/{id}")
    public ResponseEntity<?> actualizarNombreDoctor(
            @PathVariable("id") Integer id,
            @RequestBody CitaProgramadaDTO dto) {
        HttpEntity<CitaProgramadaDTO> request = new HttpEntity<>(dto, buildHeaders());
        return restTemplate.exchange(
                URL_CITAS + "/actualizar/nombre-doctor/" + id,
                HttpMethod.PUT,
                request,
                String.class);
    }

    @PostMapping("/usuarios/registrar")
    public ResponseEntity<?> registrarUsuarioDesdeAdmin(@RequestBody UsuarioDTO dto) {
        Usuario nuevo = usuarioService.registrarUsuario(dto);
        return ResponseEntity.ok(Map.of(
                "mensaje", "✅ Usuario registrado correctamente desde administrador",
                "username", nuevo.getUsername(),
                "rol", nuevo.getRol()));
    }

    @PutMapping("/citas/actualizar/nit/{id}")
    public ResponseEntity<?> actualizarNitCita(
            @PathVariable("id") Integer id,
            @RequestBody CitaProgramadaDTO dto) {
        HttpEntity<CitaProgramadaDTO> request = new HttpEntity<>(dto, buildHeaders());
        return restTemplate.exchange(
                URL_CITAS + "/actualizar/nit/" + id,
                HttpMethod.PUT,
                request,
                String.class);
    }

    @PutMapping("/citas/actualizar/colegiado/{id}")
    public ResponseEntity<?> actualizarColegiadoCita(
            @PathVariable("id") Integer id,
            @RequestBody CitaProgramadaDTO dto) {
        HttpEntity<CitaProgramadaDTO> request = new HttpEntity<>(dto, buildHeaders());
        return restTemplate.exchange(
                URL_CITAS + "/actualizar/colegiado/" + id,
                HttpMethod.PUT,
                request,
                String.class);
    }

    @PutMapping("/usuarios/{id}/cambiar-rol")
public ResponseEntity<?> cambiarRolYSecret(
        @PathVariable("id") Long id,
        @RequestBody Map<String, String> datos) {

    String nuevoRol = datos.get("rol");
    String nuevoJwtSecret = datos.get("jwtSecret");

    return usuarioService.cambiarRolYJwtSecret(id, nuevoRol, nuevoJwtSecret)
            .map(usuario -> ResponseEntity.ok("✅ Rol y JWT Secret actualizados correctamente"))
            .orElse(ResponseEntity.status(404).body("❌ Usuario no encontrado"));
}


}
