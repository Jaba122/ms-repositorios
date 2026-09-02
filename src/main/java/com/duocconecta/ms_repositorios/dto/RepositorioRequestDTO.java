package com.duocconecta.ms_repositorios.dto;

import com.duocconecta.ms_repositorios.domain.Visibilidad;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RepositorioRequestDTO(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 150)
        String nombre,

        @Size(max = 2000)
        String descripcion,

        String urlRepositorio,

        @NotNull(message = "La visibilidad es obligatoria")
        Visibilidad visibilidad,

        /** Solo se usa cuando visibilidad = COMPARTIDO. */
        List<String> colaboradoresIds
) {
}
