package com.login.roles.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/doctor")
@CrossOrigin(origins = "*")
public class DoctorController {

    @Autowired
    private RestTemplate restTemplate;

    private final String URL_PACIENTES = "http://localhost:9090/api/paciente/consultar";
    private final String URL_CITAS_ESTADO = "http://localhost:9292/api/citas/buscar/estado/";
    private final String URL_CITAS_FECHA = "http://localhost:9292/api/citas/buscar/fecha/";
    private final String URL_CITAS_HOY_COLEGIADO = "http://localhost:9292/api/citas/hoy/colegiado/";
    private final String URL_CITAS_HISTORIAL_COLEGIADO = "http://localhost:9292/api/citas/historial/colegiado/";
    private final String URL_CITAS_FECHAS_RANGO = "http://localhost:9292/api/citas/buscar/fechas/";
    private final String URL_CITAS_NIT_ESTADO = "http://localhost:9292/api/citas/buscar/nit-estado/";
    private final String URL_PACIENTES_NIT = "http://localhost:9090/api/paciente/buscar/nit/";

    @GetMapping("/pacientes")
    public ResponseEntity<?> obtenerPacientes() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(URL_PACIENTES, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al consultar pacientes: " + e.getMessage());
        }
    }

    @GetMapping("/citas/estado/{estado}")
    public ResponseEntity<?> citasPorEstado(@PathVariable("estado") String estado) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(URL_CITAS_ESTADO + estado, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al consultar citas por estado: " + e.getMessage());
        }
    }



    @GetMapping("/citas/fecha/{fecha}")
    public ResponseEntity<?> citasPorFecha(@PathVariable("fecha") String fecha) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(URL_CITAS_FECHA + fecha, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al consultar citas por fecha: " + e.getMessage());
        }
    }



    @GetMapping("/citas/hoy/colegiado/{colegiado}")
    public ResponseEntity<?> citasHoyPorColegiado(@PathVariable("colegiado") String colegiado) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(URL_CITAS_HOY_COLEGIADO + colegiado,
                    String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al consultar citas de hoy por colegiado: " + e.getMessage());
        }
    }



    @GetMapping("/citas/historial/colegiado/{colegiado}/{inicio}/{fin}")
    public ResponseEntity<?> historialCitasPorColegiado(
            @PathVariable("colegiado") String colegiado,
            @PathVariable("inicio") String inicio,
            @PathVariable("fin") String fin) {
        try {
            String url = URL_CITAS_HISTORIAL_COLEGIADO + colegiado + "/" + inicio + "/" + fin;
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al consultar historial por colegiado: " + e.getMessage());
        }
    }

    @GetMapping("/citas/rango-fechas/{inicio}/{fin}")
    public ResponseEntity<?> citasPorRangoFechas(
            @PathVariable("inicio") String inicio,
            @PathVariable("fin") String fin) {
        try {
            String url = URL_CITAS_FECHAS_RANGO + inicio + "/" + fin;
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al consultar citas por rango de fechas: " + e.getMessage());
        }
    }

    @GetMapping("/citas/nit-estado/{nit}/{estado}")
    public ResponseEntity<?> citasPorNitYEstado(@PathVariable("nit") Long nit, @PathVariable("estado") String estado) {
        try {
            String url = URL_CITAS_NIT_ESTADO + nit + "/" + estado;
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al consultar citas por NIT y estado: " + e.getMessage());
        }
    }

    @GetMapping("/pacientes/nit/{nit}")
    public ResponseEntity<?> buscarPacientesPorNit(@PathVariable("nit") Long nit) {
        try {
            String url = URL_PACIENTES_NIT + nit;
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al consultar pacientes por NIT: " + e.getMessage());
        }
    }

    @PutMapping("/cita/{id}/realizada")
    public ResponseEntity<?> cambiarEstadoRealizada(
            @PathVariable Long id,
            @RequestBody Map<String, Object> datos) {
        try {
            return restTemplate.exchange(
                    "http://localhost:9292/api/citas/finalizar/" + id,
                    HttpMethod.PUT,
                    new HttpEntity<>(datos),
                    String.class);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al finalizar la cita: " + e.getMessage());
        }
    }

}
