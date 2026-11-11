package br.com.fiap.mscontribuicoes.controller;

import br.com.fiap.mscontribuicoes.dto.request.ContribuicaoRequest;
import br.com.fiap.mscontribuicoes.dto.request.RejeicaoRequest;
import br.com.fiap.mscontribuicoes.dto.response.ContribuicaoResponse;
import br.com.fiap.mscontribuicoes.entity.StatusContribuicao;
import br.com.fiap.mscontribuicoes.service.ContribuicaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/contribuicoes")
@Tag(name = "Contribuicoes", description = "Sugestoes de obras novas e moderacao")
public class ContribuicaoController {

    private final ContribuicaoService service;

    public ContribuicaoController(ContribuicaoService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Lista contribuicoes (filtro opcional por status)")
    public ResponseEntity<List<ContribuicaoResponse>> listar(@RequestParam(required = false) StatusContribuicao status) {
        return ResponseEntity.ok(service.listar(status));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma contribuicao pelo id")
    public ResponseEntity<ContribuicaoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Sugere uma obra nova (nasce PENDENTE)")
    public ResponseEntity<ContribuicaoResponse> criar(@RequestBody @Valid ContribuicaoRequest req) {
        ContribuicaoResponse criada = service.criar(req);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(criada.id()).toUri();
        return ResponseEntity.created(uri).body(criada);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma contribuicao pendente")
    public ResponseEntity<ContribuicaoResponse> atualizar(@PathVariable Long id,
                                                          @RequestBody @Valid ContribuicaoRequest req) {
        return ResponseEntity.ok(service.atualizar(id, req));
    }

    @PatchMapping("/{id}/aprovar")
    @Operation(summary = "Aprova a contribuicao: chama o ms-obras (OpenFeign) para criar e publicar a obra")
    public ResponseEntity<ContribuicaoResponse> aprovar(@PathVariable Long id) {
        return ResponseEntity.ok(service.aprovar(id));
    }

    @PatchMapping("/{id}/rejeitar")
    @Operation(summary = "Rejeita a contribuicao informando o motivo")
    public ResponseEntity<ContribuicaoResponse> rejeitar(@PathVariable Long id,
                                                         @RequestBody @Valid RejeicaoRequest req) {
        return ResponseEntity.ok(service.rejeitar(id, req.motivo()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui uma contribuicao (nao aprovada)")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
