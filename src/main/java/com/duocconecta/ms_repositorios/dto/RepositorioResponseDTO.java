package com.duocconecta.ms_repositorios.dto;

import com.duocconecta.ms_repositorios.domain.EstadoProyecto;
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
        String sede,
        EstadoProyecto estado,
        Visibilidad visibilidad,
        List<String> colaboradoresIds,
        List<String> archivosAdjuntos,
        Instant fechaCreacion
) {
    public static RepositorioResponseDTO desdeEntidad(RepositorioProyecto r) {
        return new RepositorioResponseDTO(
                r.getId(), r.getNombre(), r.getDescripcion(), r.getUrlRepositorio(),
                r.getPropietarioId(), r.getSede(), r.getEstado(), r.getVisibilidad(),
                r.getColaboradoresIds(), r.getArchivosAdjuntos(), r.getFechaCreacion());
    }
}