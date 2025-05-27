package com.login.roles.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

@RestController
@RequestMapping("/api/recepcionista")
@CrossOrigin(origins = "*")
public class RecepcionistaController {

    @Autowired
    private RestTemplate restTemplate;

    // URLs microservicios
    private final String URL_PACIENTE_INGRESAR = "http://localhost:9090/api/paciente/ingresar";
    private final String URL_PACIENTE_CONSULTAR = "http://localhost:9090/api/paciente/consultar";

    // 📌 Adaptados a los endpoints ya existentes
    private final String URL_CITA_CREAR = "http://localhost:9292/api/citas/crear";
    private final String URL_CITA_PROGRAMADAS = "http://localhost:9292/api/citas/buscar/estado/PROGRAMADA";
    private final String URL_CITA_REALIZADAS = "http://localhost:9292/api/citas/buscar/estado/REALIZADA";
    private final String URL_CITA_CANCELADAS = "http://localhost:9292/api/citas/buscar/estado/CANCELADA";

    // Crear paciente
    @PostMapping("/paciente")
    public ResponseEntity<?> crearPaciente(@RequestBody PacienteDTO pacienteDTO) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<PacienteDTO> entity = new HttpEntity<>(pacienteDTO, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(URL_PACIENTE_INGRESAR, entity, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al crear paciente: " + e.getMessage());
        }
    }

    // Consultar pacientes
    @GetMapping("/pacientes")
    public ResponseEntity<?> consultarPacientes() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(URL_PACIENTE_CONSULTAR, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al consultar pacientes: " + e.getMessage());
        }
    }

    // Crear cita programada
    @PostMapping("/cita")
    public ResponseEntity<?> crearCita(@RequestBody CitaProgramadaDTO citaDTO) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<CitaProgramadaDTO> entity = new HttpEntity<>(citaDTO, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(URL_CITA_CREAR, entity, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al crear cita: " + e.getMessage());
        }
    }

    @PutMapping("/cita/{id}/realizada")
    public ResponseEntity<?> cambiarEstadoRealizada(@PathVariable Long id, @RequestBody Map<String, Object> datos) {
        return restTemplate.exchange("http://localhost:9292/api/citas/finalizar/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(datos),
                String.class);
    }

    @PutMapping("/cita/{id}/cancelada")
    public ResponseEntity<?> cambiarEstadoCancelada(@PathVariable("id") Long id,
            @RequestBody Map<String, Object> datos) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(datos, headers);
            restTemplate.exchange(
                    "http://localhost:9292/api/citas/cancelar/" + id,
                    HttpMethod.PUT,
                    entity,
                    String.class);
            return ResponseEntity.ok("❌ Cita marcada como cancelada");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al marcar cita como cancelada: " + e.getMessage());
        }
    }

    // Consultar citas programadas
    @GetMapping("/citas/programadas")
    public ResponseEntity<?> obtenerCitasProgramadas() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(URL_CITA_PROGRAMADAS, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al consultar citas programadas: " + e.getMessage());
        }
    }

    // Consultar citas realizadas
    @GetMapping("/citas/realizadas")
    public ResponseEntity<?> obtenerCitasRealizadas() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(URL_CITA_REALIZADAS, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al consultar citas realizadas: " + e.getMessage());
        }
    }

    // Consultar citas canceladas
    @GetMapping("/citas/canceladas")
    public ResponseEntity<?> obtenerCitasCanceladas() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(URL_CITA_CANCELADAS, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al consultar citas canceladas: " + e.getMessage());
        }
    }

    @GetMapping("/citas/nit-estado/{nit}/{estado}")
    public ResponseEntity<?> obtenerCitasPorNitYEstado(
            @PathVariable("nit") Long nit,
            @PathVariable("estado") String estado) {
        try {
            String url = "http://localhost:9292/api/citas/buscar/nit-estado/" + nit + "/" + estado;
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al consultar citas por NIT y estado: " + e.getMessage());
        }
    }

    // Consultar doctores activos
    private final String URL_DOCTORES_ACTIVOS = "http://localhost:9191/api/doctor/buscar/estado/activo";

    @GetMapping("/doctores")
    public ResponseEntity<?> obtenerDoctoresActivos() {
        try {
            ResponseEntity<DoctorDTO[]> response = restTemplate.getForEntity(URL_DOCTORES_ACTIVOS, DoctorDTO[].class);
            List<DoctorDTO> doctores = Arrays.asList(response.getBody());
            return ResponseEntity.ok(doctores);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al consultar doctores: " + e.getMessage());
        }
    }

    @GetMapping("/pacientes/nombre/{nombre}")
    public ResponseEntity<?> buscarPacientePorNombre(@PathVariable String nombre) {
        try {
            String url = "http://localhost:9090/api/paciente/buscar/nombre/" + nombre;
            ResponseEntity<PacienteDTO[]> response = restTemplate.getForEntity(url, PacienteDTO[].class);
            return ResponseEntity.ok(Arrays.asList(response.getBody()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al buscar paciente por nombre: " + e.getMessage());
        }
    }

    @GetMapping("/pacientes/nit/{nit}")
    public ResponseEntity<?> buscarPacientePorNit(@PathVariable Long nit) {
        try {
            String url = "http://localhost:9090/api/paciente/buscar/nit/" + nit;
            ResponseEntity<PacienteDTO[]> response = restTemplate.getForEntity(url, PacienteDTO[].class);
            return ResponseEntity.ok(Arrays.asList(response.getBody()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al buscar paciente por NIT: " + e.getMessage());
        }
    }

    @GetMapping("/pacientes/cui/{cui}")
    public ResponseEntity<?> buscarPacientePorCui(@PathVariable Long cui) {
        try {
            String url = "http://localhost:9090/api/paciente/buscar/cui/" + cui;
            ResponseEntity<PacienteDTO[]> response = restTemplate.getForEntity(url, PacienteDTO[].class);
            return ResponseEntity.ok(Arrays.asList(response.getBody()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al buscar paciente por CUI: " + e.getMessage());
        }
    }

    @GetMapping("/doctores/nombre/{nombre}")
    public ResponseEntity<?> buscarDoctorPorNombre(@PathVariable String nombre) {
        try {
            String url = "http://localhost:9191/api/doctor/buscar/nombre/" + nombre;
            ResponseEntity<DoctorDTO[]> response = restTemplate.getForEntity(url, DoctorDTO[].class);
            return ResponseEntity.ok(Arrays.asList(response.getBody()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al buscar doctor por nombre: " + e.getMessage());
        }
    }

    @GetMapping("/doctores/colegiado/{colegiado}")
    public ResponseEntity<?> buscarDoctorPorColegiado(@PathVariable String colegiado) {
        try {
            String url = "http://localhost:9191/api/doctor/buscar/colegiado/" + colegiado;
            ResponseEntity<DoctorDTO[]> response = restTemplate.getForEntity(url, DoctorDTO[].class);
            return ResponseEntity.ok(Arrays.asList(response.getBody()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al buscar doctor por colegiado: " + e.getMessage());
        }
    }

    @GetMapping("/doctores/especialidad/{especialidad}")
    public ResponseEntity<?> buscarDoctorPorEspecialidad(@PathVariable String especialidad) {
        try {
            String url = "http://localhost:9191/api/doctor/buscar/especialidad/" + especialidad;
            ResponseEntity<DoctorDTO[]> response = restTemplate.getForEntity(url, DoctorDTO[].class);
            return ResponseEntity.ok(Arrays.asList(response.getBody()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al buscar doctor por especialidad: " + e.getMessage());
        }
    }

    @GetMapping("/pacientes/id/{id}")
    public ResponseEntity<?> buscarPacientePorId(@PathVariable Long id) {
        try {
            String url = "http://localhost:9090/api/paciente/buscar/id/" + id;
            ResponseEntity<PacienteDTO> response = restTemplate.getForEntity(url, PacienteDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al buscar paciente por ID: " + e.getMessage());
        }
    }

    @GetMapping("/pacientes/fecha/{fecha}")
    public ResponseEntity<?> buscarPacientePorFecha(@PathVariable String fecha) {
        try {
            String url = "http://localhost:9090/api/paciente/buscar/fecha/" + fecha;
            ResponseEntity<PacienteDTO[]> response = restTemplate.getForEntity(url, PacienteDTO[].class);
            return ResponseEntity.ok(Arrays.asList(response.getBody()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al buscar paciente por fecha: " + e.getMessage());
        }
    }

    @GetMapping("/doctores/id/{id}")
    public ResponseEntity<?> buscarDoctorPorId(@PathVariable Long id) {
        try {
            String url = "http://localhost:9191/api/doctor/buscar/id/" + id;
            ResponseEntity<DoctorDTO> response = restTemplate.getForEntity(url, DoctorDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al buscar doctor por ID: " + e.getMessage());
        }
    }

    @GetMapping("/doctores/fecha/{fecha}")
    public ResponseEntity<?> buscarDoctorPorFecha(@PathVariable String fecha) {
        try {
            String url = "http://localhost:9191/api/doctor/buscar/fecha/" + fecha;
            ResponseEntity<DoctorDTO[]> response = restTemplate.getForEntity(url, DoctorDTO[].class);
            return ResponseEntity.ok(Arrays.asList(response.getBody()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al buscar doctor por fecha: " + e.getMessage());
        }
    }



}
