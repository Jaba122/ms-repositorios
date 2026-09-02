package com.duocconecta.ms_repositorios.controller;

import com.duocconecta.ms_repositorios.domain.RepositorioProyecto;
import com.duocconecta.ms_repositorios.dto.RepositorioRequestDTO;
import com.duocconecta.ms_repositorios.dto.RepositorioResponseDTO;
import com.duocconecta.ms_repositorios.service.RepositorioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/repositorios")
@RequiredArgsConstructor
public class RepositorioController {

    private final RepositorioService repositorioService;

    private String usuarioActual(HttpServletRequest request) {
        return (String) request.getAttribute("currentUserId");
    }

    @PostMapping
    public ResponseEntity<RepositorioResponseDTO> crear(@Valid @RequestBody RepositorioRequestDTO dto,
                                                          HttpServletRequest request) {
        RepositorioProyecto creado = repositorioService.crear(dto, usuarioActual(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(RepositorioResponseDTO.desdeEntidad(creado));
    }

    @GetMapping
    public ResponseEntity<List<RepositorioResponseDTO>> listar(HttpServletRequest request) {
        List<RepositorioResponseDTO> resultado = repositorioService.listarVisiblesPara(usuarioActual(request))
                .stream().map(RepositorioResponseDTO::desdeEntidad).toList();
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepositorioResponseDTO> obtener(@PathVariable UUID id, HttpServletRequest request) {
        RepositorioProyecto repo = repositorioService.obtenerSiVisible(id, usuarioActual(request));
        return ResponseEntity.ok(RepositorioResponseDTO.desdeEntidad(repo));
    }

    @PostMapping("/{id}/colaboradores")
    public ResponseEntity<RepositorioResponseDTO> agregarColaborador(@PathVariable UUID id,
                                                                       @RequestBody Map<String, String> body,
                                                                       HttpServletRequest request) {
        RepositorioProyecto actualizado = repositorioService.agregarColaborador(
                id, usuarioActual(request), body.get("colaboradorId"));
        return ResponseEntity.ok(RepositorioResponseDTO.desdeEntidad(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id, HttpServletRequest request) {
        repositorioService.eliminar(id, usuarioActual(request));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("ms-repositorios activo");
    }
}
