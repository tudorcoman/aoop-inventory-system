CREATE TABLE IF NOT EXISTS clienti (
    id serial PRIMARY KEY,
    nume VARCHAR(300) NOT NULL,
    cui BIGINT NOT NULL,
    adresa TEXT NOT NULL,
    id_persoana_contact INTEGER REFERENCES persoane(id) NOT NULL
);

INSERT INTO clienti (nume, cui, adresa, id_persoana_contact) VALUES ('Alphabet Inc.', '12345', 'Mountain View, CA', 3);
