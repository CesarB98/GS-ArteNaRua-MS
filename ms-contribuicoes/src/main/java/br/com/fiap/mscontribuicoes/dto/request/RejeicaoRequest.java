package br.com.fiap.mscontribuicoes.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RejeicaoRequest(
        @NotBlank(message = "O motivo da rejeicao e obrigatorio") @Size(max = 500) String motivo) {
}
