DROP TABLE Covid;

CREATE TABLE Covid (
    DoseNo integer NOT NULL,
    INo integer NOT NULL
);

INSERT INTO Covid VALUES (1, 1);

GRANT ALL PRIVILEGES ON Covid TO PUBLIC;