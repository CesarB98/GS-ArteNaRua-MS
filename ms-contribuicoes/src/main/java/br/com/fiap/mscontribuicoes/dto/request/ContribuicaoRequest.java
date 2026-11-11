package br.com.fiap.mscontribuicoes.dto.request;

import br.com.fiap.mscontribuicoes.entity.TipoObra;
import jakarta.validation.constraints.*;

public record ContribuicaoRequest(
        @NotBlank(message = "O titulo e obrigatorio") @Size(max = 150) String titulo,
        @Min(value = 1800, message = "Ano invalido") @Max(value = 2100, message = "Ano invalido") Integer ano,
        String historia,
        String audiodescricao,
        @NotNull(message = "A latitude e obrigatoria") @DecimalMin("-90.0") @DecimalMax("90.0") Double latitude,
        @NotNull(message = "A longitude e obrigatoria") @DecimalMin("-180.0") @DecimalMax("180.0") Double longitude,
        @Size(max = 200) String endereco,
        @NotNull(message = "O tipo e obrigatorio") TipoObra tipo,
        @NotNull(message = "O artista e obrigatorio") Long artistaId) {
}
