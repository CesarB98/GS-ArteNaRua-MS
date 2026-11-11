package br.com.fiap.mscontribuicoes.dto.response;

import br.com.fiap.mscontribuicoes.entity.Contribuicao;
import br.com.fiap.mscontribuicoes.entity.StatusContribuicao;
import br.com.fiap.mscontribuicoes.entity.TipoObra;

import java.time.LocalDateTime;

public record ContribuicaoResponse(Long id, String titulo, Integer ano, String historia, String audiodescricao,
                                   Double latitude, Double longitude, String endereco, TipoObra tipo,
                                   Long artistaId, Long obraId, StatusContribuicao status, String motivo,
                                   LocalDateTime criadoEm) {

    public static ContribuicaoResponse de(Contribuicao c) {
        return new ContribuicaoResponse(c.getId(), c.getTitulo(), c.getAno(), c.getHistoria(),
                c.getAudiodescricao(), c.getLatitude(), c.getLongitude(), c.getEndereco(), c.getTipo(),
                c.getArtistaId(), c.getObraId(), c.getStatus(), c.getMotivo(), c.getCriadoEm());
    }
}
