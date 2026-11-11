CREATE TABLE artistas (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    nome        VARCHAR(120) NOT NULL,
    bio         VARCHAR(1000),
    contato     VARCHAR(120),
    verificado  BOOLEAN      NOT NULL DEFAULT FALSE,
    criado_em   DATETIME(6)  NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE obras (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    titulo          VARCHAR(150) NOT NULL,
    ano             INT,
    historia        TEXT,
    audiodescricao  TEXT,
    latitude        DOUBLE       NOT NULL,
    longitude       DOUBLE       NOT NULL,
    endereco        VARCHAR(200),
    tipo            VARCHAR(20)  NOT NULL,
    status          VARCHAR(20)  NOT NULL,
    artista_id      BIGINT       NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_obras_artista FOREIGN KEY (artista_id) REFERENCES artistas (id)
);

CREATE INDEX idx_obras_status_geo ON obras (status, latitude, longitude);
