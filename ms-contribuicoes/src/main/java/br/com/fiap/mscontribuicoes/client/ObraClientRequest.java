package br.com.fiap.mscontribuicoes.client;

/** Contrato de entrada do ms-obras (POST /api/v1/obras). */
public record ObraClientRequest(String titulo, Integer ano, String historia, String audiodescricao,
                                Double latitude, Double longitude, String endereco, String tipo,
                                Long artistaId) {
}
