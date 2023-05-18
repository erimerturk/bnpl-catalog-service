DROP TABLE IF EXISTS property;
CREATE TABLE property
(
    id                 BIGSERIAL PRIMARY KEY NOT NULL,
    seller             varchar(255)          NOT NULL,
    price              float8                NOT NULL,
    title              varchar(255)          NOT NULL,
    version            integer               NOT NULL,
    created_date       timestamp             NOT NULL,
    last_modified_date timestamp             NOT NULL
);