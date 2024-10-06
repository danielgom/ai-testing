CREATE TABLE actors
(
    id         INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name       VARCHAR(255) NOT NULL,                          -- Nombre del actor
    occupation VARCHAR(255),                                   -- Ocupación del actor
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP -- Fecha y hora de creación del actor
);

CREATE INDEX idx_actors_name ON actors (name);

CREATE TYPE emotion_enum AS ENUM ('POSITIVE', 'NEGATIVE', 'NEUTRAL');

CREATE TABLE actor_mentions
(
    id              INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    actor_id        INT REFERENCES actors (id) ON DELETE CASCADE,
    platform_source VARCHAR(255) NOT NULL,              -- Dominio
    url_source      VARCHAR(255) NOT NULL,              -- URL de la mención
    type            VARCHAR(255) NOT NULL,              -- Tipo (Online News, Facebook Public, X, etc...)
    title           VARCHAR(255) NOT NULL,              -- Título de la mención
    sentiment       emotion_enum NOT NULL,              -- Sentimiento del actor en la mención (POSITIVE, NEGATIVE, NEUTRAL)
    content         TEXT         NOT NULL,              -- Lo que se menciono por el actor (Snippet)
    topic           VARCHAR(255) NOT NULL,              -- Tema principal de la mención (elección, title o LLM)
    mention_date    TIMESTAMP    NOT NULL,              -- Fecha y hora de la mención
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Fecha y hora de creación de la mención
);

CREATE INDEX idx_mentions_actor_id ON actor_mentions (actor_id);
CREATE INDEX idx_mentions_topic ON actor_mentions (topic);
CREATE INDEX idx_mentions_mention_date ON actor_mentions (mention_date);

CREATE TABLE actor_mention_trends
(
    id            INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    actor_id      INT REFERENCES actors (id) ON DELETE CASCADE,
    topic         VARCHAR(255) NOT NULL, -- El tema que se repite (ej. Reforma educativa)
    frequency     INT          NOT NULL, -- Número de veces que se mencionó el tema
    first_mention TIMESTAMP    NOT NULL, -- Primera vez que se mencionó el tema
    last_mention  TIMESTAMP    NOT NULL, -- Última vez que se mencionó el tema
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_mention_trends_actor_id ON actor_mention_trends (actor_id);
CREATE INDEX idx_mention_trends_topic ON actor_mention_trends (topic);

CREATE TABLE mentions -- Guardar menciones no relevantes pero Brandwatch provee
(
    id              INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    platform_source VARCHAR(255) NOT NULL,              -- Dominio
    url_source      VARCHAR(255) NOT NULL,              -- URL de la mención
    type            VARCHAR(255) NOT NULL,              -- Tipo (Online News, Facebook Public, X, etc...)
    title           VARCHAR(255) NOT NULL,              -- Título de la mención
    content         TEXT         NOT NULL,              -- Lo que se menciono
    mention_date    TIMESTAMP    NOT NULL,              -- Fecha y hora de la mención
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Fecha y hora de creación de la mención
);

CREATE TYPE alert_level_enum AS ENUM ('HIGH', 'MEDIUM', 'LOW', 'NONE');

CREATE TABLE alerts
(
    id             INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    actor_id       INT REFERENCES actors (id) ON DELETE CASCADE,
    topic          VARCHAR(255)     NOT NULL,               -- Tema que generó la alerta
    alert_level    alert_level_enum NOT NULL,               -- Nivel de criticidad (HIGH, MEDIUM, LOW)
    active         BOOLEAN          NOT NULL DEFAULT FALSE, -- Indica si la alerta está activa o no
    trigger_date   TIMESTAMP        NULL,                   -- Fecha y hora en que se disparó la alerta
    deactivated_at TIMESTAMP                                -- Fecha y hora en que se desactivó la alerta
);

CREATE INDEX idx_alerts_actor_id ON alerts (actor_id);
CREATE INDEX idx_alerts_topic ON alerts (topic);

CREATE TABLE queries
(
    id                INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    query_id          VARCHAR(255) NOT NULL,
    last_mention_date TIMESTAMP    NOT NULL
);

CREATE INDEX idx_query_query_id ON queries (query_id);