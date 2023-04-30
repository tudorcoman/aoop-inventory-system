CREATE TABLE IF NOT EXISTS furnizori (
    id serial PRIMARY KEY,
    nume VARCHAR(300) NOT NULL,
    cui BIGINT NOT NULL,
    adresa TEXT NOT NULL,
    id_persoana_contact INTEGER REFERENCES persoane(id) NOT NULL
);

INSERT INTO furnizori (nume, cui, adresa, id_persoana_contact) VALUES ('Tesla Inc.', '2468', 'Austin, TX', 4);
