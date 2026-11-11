package br.com.fiap.msobras.dto.response;

import br.com.fiap.msobras.entity.Obra;
import br.com.fiap.msobras.entity.StatusObra;
import br.com.fiap.msobras.entity.TipoObra;

public record ObraResponse(Long id, String titulo, Integer ano, String historia, String audiodescricao,
                           Double latitude, Double longitude, String endereco, TipoObra tipo,
                           StatusObra status, Long artistaId, String artistaNome) {

    public static ObraResponse de(Obra o) {
        return new ObraResponse(o.getId(), o.getTitulo(), o.getAno(), o.getHistoria(), o.getAudiodescricao(),
                o.getLatitude(), o.getLongitude(), o.getEndereco(), o.getTipo(), o.getStatus(),
                o.getArtista().getId(), o.getArtista().getNome());
    }
}
