package com.duocconecta.ms_repositorios;

import com.duocconecta.ms_repositorios.domain.RepositorioProyecto;
import com.duocconecta.ms_repositorios.domain.Visibilidad;
import com.duocconecta.ms_repositorios.exception.OperacionNoPermitidaException;
import com.duocconecta.ms_repositorios.repository.RepositorioProyectoRepository;
import com.duocconecta.ms_repositorios.service.RepositorioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RepositorioServiceTest {

    @Mock
    private RepositorioProyectoRepository repositorioProyectoRepository;

    @InjectMocks
    private RepositorioService repositorioService;

    @Test
    void esVisiblePara_repositorioPublico_esVisibleParaCualquiera() {
        RepositorioProyecto repo = RepositorioProyecto.builder()
                .propietarioId("dueño-1")
                .visibilidad(Visibilidad.PUBLICO)
                .colaboradoresIds(List.of())
                .build();

        assertThat(repo.esVisiblePara("cualquier-usuario")).isTrue();
    }

    @Test
    void esVisiblePara_repositorioPrivado_soloVisibleParaElDueño() {
        RepositorioProyecto repo = RepositorioProyecto.builder()
                .propietarioId("dueño-1")
                .visibilidad(Visibilidad.PRIVADO)
                .colaboradoresIds(List.of())
                .build();

        assertThat(repo.esVisiblePara("dueño-1")).isTrue();
        assertThat(repo.esVisiblePara("otro-usuario")).isFalse();
    }

    @Test
    void esVisiblePara_repositorioCompartido_soloVisibleParaColaboradores() {
        RepositorioProyecto repo = RepositorioProyecto.builder()
                .propietarioId("dueño-1")
                .visibilidad(Visibilidad.COMPARTIDO)
                .colaboradoresIds(List.of("colaborador-1"))
                .build();

        assertThat(repo.esVisiblePara("colaborador-1")).isTrue();
        assertThat(repo.esVisiblePara("usuario-sin-acceso")).isFalse();
    }

    @Test
    void obtenerSiVisible_deberiaRechazarAccesoNoAutorizado() {
        UUID id = UUID.randomUUID();
        RepositorioProyecto repo = RepositorioProyecto.builder()
                .id(id)
                .propietarioId("dueño-1")
                .visibilidad(Visibilidad.PRIVADO)
                .colaboradoresIds(List.of())
                .build();

        when(repositorioProyectoRepository.findById(id)).thenReturn(Optional.of(repo));

        assertThatThrownBy(() -> repositorioService.obtenerSiVisible(id, "usuario-sin-acceso"))
                .isInstanceOf(OperacionNoPermitidaException.class);
    }
}
