package com.duocconecta.ms_repositorios.service;

import com.duocconecta.ms_repositorios.domain.RepositorioProyecto;
import com.duocconecta.ms_repositorios.domain.Visibilidad;
import com.duocconecta.ms_repositorios.dto.RepositorioRequestDTO;
import com.duocconecta.ms_repositorios.exception.OperacionNoPermitidaException;
import com.duocconecta.ms_repositorios.exception.RecursoNoEncontradoException;
import com.duocconecta.ms_repositorios.repository.RepositorioProyectoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RepositorioService {

    private final RepositorioProyectoRepository repositorioProyectoRepository;

    public RepositorioProyecto crear(RepositorioRequestDTO dto, String propietarioId) {
        RepositorioProyecto repo = RepositorioProyecto.builder()
                .nombre(dto.nombre())
                .descripcion(dto.descripcion())
                .urlRepositorio(dto.urlRepositorio())
                .propietarioId(propietarioId)
                .sede(dto.sede())
                .estado(dto.estado())
                .visibilidad(dto.visibilidad())
                .colaboradoresIds(dto.visibilidad() == Visibilidad.COMPARTIDO && dto.colaboradoresIds() != null
                        ? dto.colaboradoresIds() : List.of())
                .archivosAdjuntos(dto.archivosAdjuntos() != null ? dto.archivosAdjuntos() : List.of())
                .build();
        return repositorioProyectoRepository.save(repo);
    }

    /**
     * Lista solo lo que el usuario autenticado puede ver:
     * todos los públicos + sus propios privados/compartidos + los compartidos donde es colaborador.
     */
    public List<RepositorioProyecto> listarVisiblesPara(String usuarioId) {
        List<RepositorioProyecto> publicos = repositorioProyectoRepository.findByVisibilidad(Visibilidad.PUBLICO);
        List<RepositorioProyecto> propios = repositorioProyectoRepository.findByPropietarioId(usuarioId);
        List<RepositorioProyecto> compartidosConmigo = repositorioProyectoRepository
                .findByVisibilidadAndColaboradoresIdsContaining(Visibilidad.COMPARTIDO, usuarioId);

        return Stream.of(publicos, propios, compartidosConmigo)
                .flatMap(List::stream)
                .distinct()
                .toList();
    }

    public RepositorioProyecto obtenerSiVisible(UUID id, String usuarioId) {
        RepositorioProyecto repo = repositorioProyectoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Repositorio no encontrado: " + id));

        if (!repo.esVisiblePara(usuarioId)) {
            throw new OperacionNoPermitidaException("No tienes acceso a este repositorio");
        }
        return repo;
    }

    public RepositorioProyecto agregarColaborador(UUID id, String usuarioId, String nuevoColaboradorId) {
        RepositorioProyecto repo = repositorioProyectoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Repositorio no encontrado: " + id));

        if (!repo.getPropietarioId().equals(usuarioId)) {
            throw new OperacionNoPermitidaException("Solo el propietario puede agregar colaboradores");
        }
        if (repo.getVisibilidad() != Visibilidad.COMPARTIDO) {
            throw new OperacionNoPermitidaException("Solo se pueden agregar colaboradores a repositorios compartidos");
        }
        if (!repo.getColaboradoresIds().contains(nuevoColaboradorId)) {
            repo.getColaboradoresIds().add(nuevoColaboradorId);
        }
        return repositorioProyectoRepository.save(repo);
    }

    public void eliminar(UUID id, String usuarioId) {
        RepositorioProyecto repo = repositorioProyectoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Repositorio no encontrado: " + id));

        if (!repo.getPropietarioId().equals(usuarioId)) {
            throw new OperacionNoPermitidaException("Solo el propietario puede eliminar este repositorio");
        }
        repositorioProyectoRepository.delete(repo);
    }
}
