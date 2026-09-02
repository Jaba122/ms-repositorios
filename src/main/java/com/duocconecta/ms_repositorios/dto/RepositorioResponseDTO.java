package com.duocconecta.ms_repositorios.dto;

import com.duocconecta.ms_repositorios.domain.RepositorioProyecto;
import com.duocconecta.ms_repositorios.domain.Visibilidad;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record RepositorioResponseDTO(
        UUID id,
        String nombre,
        String descripcion,
        String urlRepositorio,
        String propietarioId,
        Visibilidad visibilidad,
        List<String> colaboradoresIds,
        Instant fechaCreacion
) {
    public static RepositorioResponseDTO desdeEntidad(RepositorioProyecto r) {
        return new RepositorioResponseDTO(
                r.getId(), r.getNombre(), r.getDescripcion(), r.getUrlRepositorio(),
                r.getPropietarioId(), r.getVisibilidad(), r.getColaboradoresIds(), r.getFechaCreacion());
    }
}
