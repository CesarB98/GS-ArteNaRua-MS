CREATE TABLE contribuicoes (
    id              BIGINT        NOT NULL AUTO_INCREMENT,
    titulo          VARCHAR(150)  NOT NULL,
    ano             INT,
    historia        TEXT,
    audiodescricao  TEXT,
    latitude        DOUBLE        NOT NULL,
    longitude       DOUBLE        NOT NULL,
    endereco        VARCHAR(200),
    tipo            VARCHAR(20)   NOT NULL,
    artista_id      BIGINT        NOT NULL,
    obra_id         BIGINT,
    status          VARCHAR(20)   NOT NULL,
    motivo          VARCHAR(500),
    criado_em       DATETIME(6)   NOT NULL,
    PRIMARY KEY (id)
);

CREATE INDEX idx_contribuicoes_status ON contribuicoes (status);
