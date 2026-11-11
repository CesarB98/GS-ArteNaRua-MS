package br.com.fiap.mscontribuicoes.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contribuicoes")
public class Contribuicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    private Integer ano;

    @Column(columnDefinition = "TEXT")
    private String historia;

    @Column(columnDefinition = "TEXT")
    private String audiodescricao;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(length = 200)
    private String endereco;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoObra tipo;

    /** Artista (id no ms-obras). Nao ha FK: cada microsservico tem seu proprio banco. */
    @Column(name = "artista_id", nullable = false)
    private Long artistaId;

    /** Id da obra criada no ms-obras quando a contribuicao e aprovada. */
    @Column(name = "obra_id")
    private Long obraId;

    /** Status da contribuicao: vira APROVADA somente apos a comunicacao sincrona com o ms-obras. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusContribuicao status = StatusContribuicao.PENDENTE;

    @Column(length = 500)
    private String motivo;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    void aoCriar() {
        this.criadoEm = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public Integer getAno() { return ano; }
    public void setAno(Integer ano) { this.ano = ano; }
    public String getHistoria() { return historia; }
    public void setHistoria(String historia) { this.historia = historia; }
    public String getAudiodescricao() { return audiodescricao; }
    public void setAudiodescricao(String audiodescricao) { this.audiodescricao = audiodescricao; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public TipoObra getTipo() { return tipo; }
    public void setTipo(TipoObra tipo) { this.tipo = tipo; }
    public Long getArtistaId() { return artistaId; }
    public void setArtistaId(Long artistaId) { this.artistaId = artistaId; }
    public Long getObraId() { return obraId; }
    public void setObraId(Long obraId) { this.obraId = obraId; }
    public StatusContribuicao getStatus() { return status; }
    public void setStatus(StatusContribuicao status) { this.status = status; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
}
