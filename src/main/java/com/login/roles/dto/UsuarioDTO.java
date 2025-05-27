package com.login.roles.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {

    private Long id;
    private String username;
    private String password;
    private String rol;
    private String estado;
    private LocalDate fechaCreacion;
    private LocalTime horaCreacion;
    private String jwtSecret;

    public UsuarioDTO(String username, String password, String rol, String jwtSecret) {
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.jwtSecret = jwtSecret;
    }

}
