package br.com.fiap.mscontribuicoes.repository;

import br.com.fiap.mscontribuicoes.entity.Contribuicao;
import br.com.fiap.mscontribuicoes.entity.StatusContribuicao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContribuicaoRepository extends JpaRepository<Contribuicao, Long> {

    List<Contribuicao> findByStatus(StatusContribuicao status);
}
