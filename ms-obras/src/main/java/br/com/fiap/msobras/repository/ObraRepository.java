package br.com.fiap.msobras.repository;

import br.com.fiap.msobras.entity.Obra;
import br.com.fiap.msobras.entity.StatusObra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ObraRepository extends JpaRepository<Obra, Long> {

    boolean existsByArtistaId(Long artistaId);

    List<Obra> findByStatusAndLatitudeBetweenAndLongitudeBetween(
            StatusObra status, double latMin, double latMax, double lngMin, double lngMax);
}
