CREATE TABLE actors
(
    id         INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name       VARCHAR(255) NOT NULL,                          -- Nombre del actor
    occupation VARCHAR(255),                                   -- Ocupación del actor
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP -- Fecha y hora de creación del actor
);

CREATE INDEX idx_actors_name ON actors (name);

CREATE TABLE mentions
(
    id              INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    actor_id        INT REFERENCES actors (id) ON DELETE CASCADE,
    platform_source VARCHAR(255) NOT NULL,              -- Dominio
    url_source      VARCHAR(255) NOT NULL,              -- URL de la mención
    type            VARCHAR(255) NOT NULL,              -- Tipo (Online News, Facebook Public, X, etc...)
    content         TEXT         NOT NULL,              -- Lo que mencionó el actor (Snippet)
    topic           VARCHAR(255) NOT NULL,              -- Tema principal de la mención (elección, title o LLM)
    mention_date    TIMESTAMP    NOT NULL,              -- Fecha y hora de la mención
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Fecha y hora de creación de la mención
);

CREATE INDEX idx_mentions_actor_id ON mentions (actor_id);
CREATE INDEX idx_mentions_topic ON mentions (topic);
CREATE INDEX idx_mentions_mention_date ON mentions (mention_date);

CREATE TABLE mention_trends
(
    id            INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    actor_id      INT REFERENCES actors (id) ON DELETE CASCADE,
    topic         VARCHAR(255) NOT NULL, -- El tema que se repite (ej. Reforma educativa)
    frequency     INT          NOT NULL, -- Número de veces que se mencionó el tema
    first_mention TIMESTAMP    NOT NULL, -- Primera vez que se mencionó el tema
    last_mention  TIMESTAMP    NOT NULL, -- Última vez que se mencionó el tema
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_mention_trends_actor_id ON mention_trends (actor_id);
CREATE INDEX idx_mention_trends_topic ON mention_trends (topic);

CREATE TYPE alert_level_enum AS ENUM ('HIGH', 'MEDIUM', 'LOW');

CREATE TABLE alerts
(
    id             INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    actor_id       INT REFERENCES actors (id) ON DELETE CASCADE,
    topic          VARCHAR(255) NOT NULL,              -- Tema que generó la alerta
    alert_level    alert_level_enum,                   -- Nivel de criticidad (HIGH, MEDIUM, LOW)
    active         BOOLEAN      NOT NULL DEFAULT TRUE, -- Indica si la alerta está activa o no
    trigger_date   TIMESTAMP    NOT NULL,              -- Fecha y hora en que se disparó la alerta
    deactivated_at TIMESTAMP                           -- Fecha y hora en que se desactivó la alerta
);

CREATE INDEX idx_alerts_actor_id ON alerts (actor_id);

CREATE TABLE query
(
    id                INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    query_id          INTEGER   NOT NULL,
    last_mention_date TIMESTAMP NOT NULL
);

CREATE INDEX idx_query_query_id ON query (query_id);