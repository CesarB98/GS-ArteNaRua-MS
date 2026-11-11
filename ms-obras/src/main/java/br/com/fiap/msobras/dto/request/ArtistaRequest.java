package br.com.fiap.msobras.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ArtistaRequest(
        @NotBlank(message = "O nome e obrigatorio") @Size(max = 120) String nome,
        @Size(max = 1000) String bio,
        @Size(max = 120) String contato,
        Boolean verificado) {
}
