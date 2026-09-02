package com.duocconecta.ms_repositorios.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "repositorios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepositorioProyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 2000)
    private String descripcion;

    @Column(name = "url_repositorio")
    private String urlRepositorio;

    @Column(name = "propietario_id", nullable = false, length = 100)
    private String propietarioId;

    @Column(length = 80)
    private String sede;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoProyecto estado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Visibilidad visibilidad;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "repositorio_colaboradores", joinColumns = @JoinColumn(name = "repositorio_id"))
    @Column(name = "usuario_id")
    private List<String> colaboradoresIds = new ArrayList<>();

    /** Documentos/capturas adicionales, aparte del link al repo (útil para patrones, guías, etc). */
    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "repositorio_archivos", joinColumns = @JoinColumn(name = "repositorio_id"))
    @Column(name = "url_archivo")
    private List<String> archivosAdjuntos = new ArrayList<>();

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private Instant fechaCreacion;

    @PrePersist
    void alPersistir() {
        this.fechaCreacion = Instant.now();
    }

    public boolean esVisiblePara(String usuarioId) {
        if (visibilidad == Visibilidad.PUBLICO) {
            return true;
        }
        if (propietarioId.equals(usuarioId)) {
            return true;
        }
        return visibilidad == Visibilidad.COMPARTIDO && colaboradoresIds.contains(usuarioId);
    }
}