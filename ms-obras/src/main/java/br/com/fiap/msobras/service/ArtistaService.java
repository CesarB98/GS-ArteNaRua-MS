package br.com.fiap.msobras.service;

import br.com.fiap.msobras.dto.request.ArtistaRequest;
import br.com.fiap.msobras.dto.response.ArtistaResponse;
import br.com.fiap.msobras.entity.Artista;
import br.com.fiap.msobras.exception.RecursoNaoEncontradoException;
import br.com.fiap.msobras.exception.RegraNegocioException;
import br.com.fiap.msobras.repository.ArtistaRepository;
import br.com.fiap.msobras.repository.ObraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ArtistaService {

    private final ArtistaRepository artistaRepository;
    private final ObraRepository obraRepository;

    public ArtistaService(ArtistaRepository artistaRepository, ObraRepository obraRepository) {
        this.artistaRepository = artistaRepository;
        this.obraRepository = obraRepository;
    }

    @Transactional(readOnly = true)
    public List<ArtistaResponse> listar() {
        return artistaRepository.findAll().stream().map(ArtistaResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public ArtistaResponse buscarPorId(Long id) {
        return ArtistaResponse.de(obter(id));
    }

    @Transactional
    public ArtistaResponse criar(ArtistaRequest req) {
        Artista artista = new Artista();
        copiar(req, artista);
        return ArtistaResponse.de(artistaRepository.save(artista));
    }

    @Transactional
    public ArtistaResponse atualizar(Long id, ArtistaRequest req) {
        Artista artista = obter(id);
        copiar(req, artista);
        return ArtistaResponse.de(artistaRepository.save(artista));
    }

    @Transactional
    public void excluir(Long id) {
        Artista artista = obter(id);
        if (obraRepository.existsByArtistaId(id)) {
            throw new RegraNegocioException("Nao e possivel excluir um artista que possui obras cadastradas");
        }
        artistaRepository.delete(artista);
    }

    private Artista obter(Long id) {
        return artistaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Artista", id));
    }

    private void copiar(ArtistaRequest req, Artista artista) {
        artista.setNome(req.nome());
        artista.setBio(req.bio());
        artista.setContato(req.contato());
        artista.setVerificado(Boolean.TRUE.equals(req.verificado()));
    }
}
