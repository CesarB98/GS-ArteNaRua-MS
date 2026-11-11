package br.com.fiap.msobras.repository;

import br.com.fiap.msobras.entity.Artista;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistaRepository extends JpaRepository<Artista, Long> {
}
