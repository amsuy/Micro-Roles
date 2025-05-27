package com.login.roles.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {
    private Long iddoctor;
    private String colegiado;
    private String nombrecompleto;
    private String especialidad;
    private LocalDate fecharegistro;
    private String direccion;
    private String centrohospitalario;
    private int edad;
    private String observacion;
    private String estado;
}
