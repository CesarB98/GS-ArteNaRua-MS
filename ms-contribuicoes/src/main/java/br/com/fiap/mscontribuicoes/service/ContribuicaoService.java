package br.com.fiap.mscontribuicoes.service;

import br.com.fiap.mscontribuicoes.client.ObraClientRequest;
import br.com.fiap.mscontribuicoes.client.ObraClientResponse;
import br.com.fiap.mscontribuicoes.client.ObrasClient;
import br.com.fiap.mscontribuicoes.client.StatusObraClientRequest;
import br.com.fiap.mscontribuicoes.dto.request.ContribuicaoRequest;
import br.com.fiap.mscontribuicoes.dto.response.ContribuicaoResponse;
import br.com.fiap.mscontribuicoes.entity.Contribuicao;
import br.com.fiap.mscontribuicoes.entity.StatusContribuicao;
import br.com.fiap.mscontribuicoes.exception.RecursoNaoEncontradoException;
import br.com.fiap.mscontribuicoes.exception.RegraNegocioException;
import br.com.fiap.mscontribuicoes.repository.ContribuicaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContribuicaoService {

    private final ContribuicaoRepository repository;
    private final ObrasClient obrasClient;

    public ContribuicaoService(ContribuicaoRepository repository, ObrasClient obrasClient) {
        this.repository = repository;
        this.obrasClient = obrasClient;
    }

    @Transactional(readOnly = true)
    public List<ContribuicaoResponse> listar(StatusContribuicao status) {
        List<Contribuicao> lista = status == null ? repository.findAll() : repository.findByStatus(status);
        return lista.stream().map(ContribuicaoResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public ContribuicaoResponse buscarPorId(Long id) {
        return ContribuicaoResponse.de(obter(id));
    }

    @Transactional
    public ContribuicaoResponse criar(ContribuicaoRequest req) {
        Contribuicao c = new Contribuicao();
        copiar(req, c);
        c.setStatus(StatusContribuicao.PENDENTE);
        return ContribuicaoResponse.de(repository.save(c));
    }

    @Transactional
    public ContribuicaoResponse atualizar(Long id, ContribuicaoRequest req) {
        Contribuicao c = obter(id);
        exigirPendente(c);
        copiar(req, c);
        return ContribuicaoResponse.de(repository.save(c));
    }

    @Transactional
    public void excluir(Long id) {
        Contribuicao c = obter(id);
        if (c.getStatus() == StatusContribuicao.APROVADA) {
            throw new RegraNegocioException("Uma contribuicao aprovada nao pode ser excluida");
        }
        repository.delete(c);
    }

    /**
     * Aprova a contribuicao. A mudanca de status depende da comunicacao SINCRONA com o ms-obras:
     * cria a obra (se ainda nao existe) e a publica. Se o ms-obras falhar, o fallback lanca
     * excecao e a contribuicao continua PENDENTE (sem @Transactional: o obraId ja criado e
     * mantido, tornando a nova tentativa idempotente).
     */
    public ContribuicaoResponse aprovar(Long id) {
        Contribuicao c = obter(id);
        exigirPendente(c);

        if (c.getObraId() == null) {
            ObraClientResponse obra = obrasClient.criarObra(new ObraClientRequest(
                    c.getTitulo(), c.getAno(), c.getHistoria(), c.getAudiodescricao(),
                    c.getLatitude(), c.getLongitude(), c.getEndereco(), c.getTipo().name(), c.getArtistaId()));
            c.setObraId(obra.id());
            repository.save(c);
        }

        obrasClient.alterarStatus(c.getObraId(), new StatusObraClientRequest("PUBLICADA"));

        c.setStatus(StatusContribuicao.APROVADA);
        c.setMotivo(null);
        return ContribuicaoResponse.de(repository.save(c));
    }

    @Transactional
    public ContribuicaoResponse rejeitar(Long id, String motivo) {
        Contribuicao c = obter(id);
        exigirPendente(c);
        c.setStatus(StatusContribuicao.REJEITADA);
        c.setMotivo(motivo);
        return ContribuicaoResponse.de(repository.save(c));
    }

    private Contribuicao obter(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Contribuicao", id));
    }

    private void exigirPendente(Contribuicao c) {
        if (c.getStatus() != StatusContribuicao.PENDENTE) {
            throw new RegraNegocioException("A contribuicao esta " + c.getStatus() + " e nao pode mais ser alterada");
        }
    }

    private void copiar(ContribuicaoRequest req, Contribuicao c) {
        c.setTitulo(req.titulo());
        c.setAno(req.ano());
        c.setHistoria(req.historia());
        c.setAudiodescricao(req.audiodescricao());
        c.setLatitude(req.latitude());
        c.setLongitude(req.longitude());
        c.setEndereco(req.endereco());
        c.setTipo(req.tipo());
        c.setArtistaId(req.artistaId());
    }
}
