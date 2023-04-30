
CREATE TABLE IF NOT EXISTS angajati (
    id INTEGER REFERENCES persoane(id) UNIQUE NOT NULL,
    manager_id INTEGER REFERENCES angajati(id)
);

INSERT INTO angajati (id, manager_id) VALUES (1, NULL), (2, 1);