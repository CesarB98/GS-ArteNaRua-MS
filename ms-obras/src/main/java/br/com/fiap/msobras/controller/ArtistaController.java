package br.com.fiap.msobras.controller;

import br.com.fiap.msobras.dto.request.ArtistaRequest;
import br.com.fiap.msobras.dto.response.ArtistaResponse;
import br.com.fiap.msobras.service.ArtistaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/artistas")
@Tag(name = "Artistas", description = "CRUD de artistas")
public class ArtistaController {

    private final ArtistaService service;

    public ArtistaController(ArtistaService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Lista todos os artistas")
    public ResponseEntity<List<ArtistaResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um artista pelo id")
    public ResponseEntity<ArtistaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Cria um artista")
    public ResponseEntity<ArtistaResponse> criar(@RequestBody @Valid ArtistaRequest req) {
        ArtistaResponse criado = service.criar(req);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(uri).body(criado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um artista")
    public ResponseEntity<ArtistaResponse> atualizar(@PathVariable Long id, @RequestBody @Valid ArtistaRequest req) {
        return ResponseEntity.ok(service.atualizar(id, req));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui um artista (somente se nao tiver obras)")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
