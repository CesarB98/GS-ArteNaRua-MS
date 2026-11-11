package br.com.fiap.msobras.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "artistas")
public class Artista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(length = 1000)
    private String bio;

    @Column(length = 120)
    private String contato;

    @Column(nullable = false)
    private boolean verificado;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    void aoCriar() {
        this.criadoEm = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public String getContato() { return contato; }
    public void setContato(String contato) { this.contato = contato; }
    public boolean isVerificado() { return verificado; }
    public void setVerificado(boolean verificado) { this.verificado = verificado; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
}
