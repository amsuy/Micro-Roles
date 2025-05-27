package com.login.roles.entidad;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 30)
    private String rol;

    @Column(name = "fecha_creacion", nullable = false, updatable = false, insertable = false)
    private LocalDate fechaCreacion;

    @Column(name = "hora_creacion", nullable = false, updatable = false, insertable = false)
    private LocalTime horaCreacion;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(name = "jwt_secret")
    private String jwtSecret;

}
