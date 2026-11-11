package br.com.fiap.msobras.dto.request;

import br.com.fiap.msobras.entity.StatusObra;
import jakarta.validation.constraints.NotNull;

public record StatusObraRequest(@NotNull(message = "O status e obrigatorio") StatusObra status) {
}
