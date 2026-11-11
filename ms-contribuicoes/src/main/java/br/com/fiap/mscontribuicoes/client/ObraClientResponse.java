package br.com.fiap.mscontribuicoes.client;

/** Contrato de saida do ms-obras (apenas os campos que o ms-contribuicoes usa). */
public record ObraClientResponse(Long id, String titulo, String status) {
}
