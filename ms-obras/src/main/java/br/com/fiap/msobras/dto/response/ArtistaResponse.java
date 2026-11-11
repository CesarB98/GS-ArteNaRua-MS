package br.com.fiap.msobras.dto.response;

import br.com.fiap.msobras.entity.Artista;

public record ArtistaResponse(Long id, String nome, String bio, String contato, boolean verificado) {

    public static ArtistaResponse de(Artista a) {
        return new ArtistaResponse(a.getId(), a.getNome(), a.getBio(), a.getContato(), a.isVerificado());
    }
}
