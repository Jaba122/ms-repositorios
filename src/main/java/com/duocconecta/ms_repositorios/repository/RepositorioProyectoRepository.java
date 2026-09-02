package com.duocconecta.ms_repositorios.repository;

import com.duocconecta.ms_repositorios.domain.RepositorioProyecto;
import com.duocconecta.ms_repositorios.domain.Visibilidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RepositorioProyectoRepository extends JpaRepository<RepositorioProyecto, UUID> {

    List<RepositorioProyecto> findByVisibilidad(Visibilidad visibilidad);

    List<RepositorioProyecto> findByPropietarioId(String propietarioId);

    List<RepositorioProyecto> findByVisibilidadAndColaboradoresIdsContaining(Visibilidad visibilidad, String usuarioId);
}
