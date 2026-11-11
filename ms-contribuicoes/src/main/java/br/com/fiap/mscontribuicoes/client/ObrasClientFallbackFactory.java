package br.com.fiap.mscontribuicoes.client;

import br.com.fiap.mscontribuicoes.exception.RecursoNaoEncontradoException;
import br.com.fiap.mscontribuicoes.exception.RegraNegocioException;
import br.com.fiap.mscontribuicoes.exception.ServicoIndisponivelException;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * Fallback do Circuit Breaker. Erros 4xx do ms-obras viram erros de negocio coerentes;
 * qualquer outra falha (servico fora do ar, timeout, circuito aberto) vira 503 e a
 * contribuicao permanece PENDENTE, podendo ser aprovada de novo depois.
 */
@Component
public class ObrasClientFallbackFactory implements FallbackFactory<ObrasClient> {

    private static final Logger log = LoggerFactory.getLogger(ObrasClientFallbackFactory.class);

    @Override
    public ObrasClient create(Throwable cause) {
        return new ObrasClient() {
            @Override
            public ObraClientResponse criarObra(ObraClientRequest request) {
                throw traduzir(cause);
            }

            @Override
            public ObraClientResponse alterarStatus(Long id, StatusObraClientRequest request) {
                throw traduzir(cause);
            }
        };
    }

    private RuntimeException traduzir(Throwable cause) {
        log.warn("Fallback acionado para chamada ao ms-obras: {}", cause.toString());
        if (cause instanceof FeignException fe) {
            int status = fe.status();
            if (status == 404) {
                return new RecursoNaoEncontradoException("Recurso nao encontrado no ms-obras (verifique artistaId/obraId)");
            }
            if (status == 409 || status == 400) {
                return new RegraNegocioException("O ms-obras rejeitou a operacao (HTTP " + status + ")");
            }
        }
        return new ServicoIndisponivelException(
                "O servico de obras esta indisponivel no momento. A contribuicao continua pendente; tente novamente.");
    }
}
