package br.com.fiap.msobras.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "obras")
public class Obra {

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

    /** Status da obra: alterado pelo ms-contribuicoes via comunicacao sincrona (OpenFeign). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusObra status = StatusObra.EM_MODERACAO;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "artista_id", nullable = false)
    private Artista artista;

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
    public StatusObra getStatus() { return status; }
    public void setStatus(StatusObra status) { this.status = status; }
    public Artista getArtista() { return artista; }
    public void setArtista(Artista artista) { this.artista = artista; }
}
