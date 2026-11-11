package br.com.fiap.mscontribuicoes.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Cliente OpenFeign do ms-obras. Usa o NOME do servico registrado no Eureka (sem URL fixa);
 * o Spring Cloud LoadBalancer escolhe a instancia. O Circuit Breaker aciona o fallback.
 */
@FeignClient(name = "ms-obras", fallbackFactory = ObrasClientFallbackFactory.class)
public interface ObrasClient {

    @PostMapping("/api/v1/obras")
    ObraClientResponse criarObra(@RequestBody ObraClientRequest request);

    @PatchMapping("/api/v1/obras/{id}/status")
    ObraClientResponse alterarStatus(@PathVariable("id") Long id, @RequestBody StatusObraClientRequest request);
}
