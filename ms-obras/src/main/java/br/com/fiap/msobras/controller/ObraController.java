package br.com.fiap.msobras.controller;

import br.com.fiap.msobras.dto.request.ObraRequest;
import br.com.fiap.msobras.dto.request.StatusObraRequest;
import br.com.fiap.msobras.dto.response.ObraResponse;
import br.com.fiap.msobras.service.ObraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/obras")
@Tag(name = "Obras", description = "Catalogo de obras de arte urbana")
public class ObraController {

    private final ObraService service;

    public ObraController(ObraService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Lista todas as obras")
    public ResponseEntity<List<ObraResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/proximas")
    @Operation(summary = "Lista obras publicadas proximas de uma posicao (raio em metros, padrao 100)")
    public ResponseEntity<List<ObraResponse>> proximas(@RequestParam double lat,
                                                       @RequestParam double lng,
                                                       @RequestParam(defaultValue = "100") double raioMetros) {
        return ResponseEntity.ok(service.listarProximas(lat, lng, raioMetros));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma obra pelo id")
    public ResponseEntity<ObraResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Cria uma obra (nasce EM_MODERACAO)")
    public ResponseEntity<ObraResponse> criar(@RequestBody @Valid ObraRequest req) {
        ObraResponse criada = service.criar(req);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(criada.id()).toUri();
        return ResponseEntity.created(uri).body(criada);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza os dados de uma obra")
    public ResponseEntity<ObraResponse> atualizar(@PathVariable Long id, @RequestBody @Valid ObraRequest req) {
        return ResponseEntity.ok(service.atualizar(id, req));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Altera o status da obra (usado pelo ms-contribuicoes via OpenFeign)")
    public ResponseEntity<ObraResponse> alterarStatus(@PathVariable Long id,
                                                      @RequestBody @Valid StatusObraRequest req) {
        return ResponseEntity.ok(service.alterarStatus(id, req.status()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui uma obra")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
