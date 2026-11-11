package br.com.fiap.msobras.service;

import br.com.fiap.msobras.dto.request.ObraRequest;
import br.com.fiap.msobras.dto.response.ObraResponse;
import br.com.fiap.msobras.entity.Artista;
import br.com.fiap.msobras.entity.Obra;
import br.com.fiap.msobras.entity.StatusObra;
import br.com.fiap.msobras.exception.RecursoNaoEncontradoException;
import br.com.fiap.msobras.exception.RegraNegocioException;
import br.com.fiap.msobras.repository.ArtistaRepository;
import br.com.fiap.msobras.repository.ObraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ObraService {

    private static final double METROS_POR_GRAU_LAT = 111_320.0;
    private static final double RAIO_TERRA_METROS = 6_371_000.0;

    /** Transicoes permitidas do status da obra. */
    private static final Map<StatusObra, Set<StatusObra>> TRANSICOES = Map.of(
            StatusObra.EM_MODERACAO, Set.of(StatusObra.PUBLICADA, StatusObra.REMOVIDA),
            StatusObra.PUBLICADA, Set.of(StatusObra.REMOVIDA),
            StatusObra.REMOVIDA, Set.of());

    private final ObraRepository obraRepository;
    private final ArtistaRepository artistaRepository;

    public ObraService(ObraRepository obraRepository, ArtistaRepository artistaRepository) {
        this.obraRepository = obraRepository;
        this.artistaRepository = artistaRepository;
    }

    @Transactional(readOnly = true)
    public List<ObraResponse> listar() {
        return obraRepository.findAll().stream().map(ObraResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public ObraResponse buscarPorId(Long id) {
        return ObraResponse.de(obter(id));
    }

    /** Obras PUBLICADAS num raio (em metros) ao redor de uma posicao, da mais proxima para a mais distante. */
    @Transactional(readOnly = true)
    public List<ObraResponse> listarProximas(double lat, double lng, double raioMetros) {
        double deltaLat = raioMetros / METROS_POR_GRAU_LAT;
        double deltaLng = raioMetros / (METROS_POR_GRAU_LAT * Math.max(Math.cos(Math.toRadians(lat)), 0.01));
        return obraRepository.findByStatusAndLatitudeBetweenAndLongitudeBetween(
                        StatusObra.PUBLICADA, lat - deltaLat, lat + deltaLat, lng - deltaLng, lng + deltaLng)
                .stream()
                .filter(o -> distanciaMetros(lat, lng, o.getLatitude(), o.getLongitude()) <= raioMetros)
                .sorted((a, b) -> Double.compare(
                        distanciaMetros(lat, lng, a.getLatitude(), a.getLongitude()),
                        distanciaMetros(lat, lng, b.getLatitude(), b.getLongitude())))
                .map(ObraResponse::de)
                .toList();
    }

    @Transactional
    public ObraResponse criar(ObraRequest req) {
        Obra obra = new Obra();
        copiar(req, obra);
        obra.setStatus(StatusObra.EM_MODERACAO);
        return ObraResponse.de(obraRepository.save(obra));
    }

    @Transactional
    public ObraResponse atualizar(Long id, ObraRequest req) {
        Obra obra = obter(id);
        copiar(req, obra);
        return ObraResponse.de(obraRepository.save(obra));
    }

    @Transactional
    public void excluir(Long id) {
        obraRepository.delete(obter(id));
    }

    /** Chamado pelo ms-contribuicoes (OpenFeign) para publicar ou remover uma obra. */
    @Transactional
    public ObraResponse alterarStatus(Long id, StatusObra novoStatus) {
        Obra obra = obter(id);
        if (!TRANSICOES.get(obra.getStatus()).contains(novoStatus)) {
            throw new RegraNegocioException("Transicao de status invalida: "
                    + obra.getStatus() + " -> " + novoStatus);
        }
        obra.setStatus(novoStatus);
        return ObraResponse.de(obraRepository.save(obra));
    }

    private Obra obter(Long id) {
        return obraRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Obra", id));
    }

    private void copiar(ObraRequest req, Obra obra) {
        Artista artista = artistaRepository.findById(req.artistaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Artista", req.artistaId()));
        obra.setTitulo(req.titulo());
        obra.setAno(req.ano());
        obra.setHistoria(req.historia());
        obra.setAudiodescricao(req.audiodescricao());
        obra.setLatitude(req.latitude());
        obra.setLongitude(req.longitude());
        obra.setEndereco(req.endereco());
        obra.setTipo(req.tipo());
        obra.setArtista(artista);
    }

    /** Distancia de Haversine em metros. */
    static double distanciaMetros(double lat1, double lng1, double lat2, double lng2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        return RAIO_TERRA_METROS * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
