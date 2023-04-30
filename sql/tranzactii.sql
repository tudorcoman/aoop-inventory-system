DROP TYPE IF EXISTS transaction_types;
CREATE TYPE transaction_types AS ENUM( 'IN', 'OUT');

CREATE TABLE IF NOT EXISTS tranzactii (
    id serial PRIMARY KEY,
    product_id INTEGER REFERENCES produse(id),
    depozit_id INTEGER REFERENCES depozite(id),
    partener_id VARCHAR(15) NOT NULL,
    quantity NUMERIC(8, 2) NOT NULL,
    tip transaction_types,
    data_tranzactie TIMESTAMP DEFAULT current_timestamp
);

INSERT INTO tranzactii (product_id, depozit_id, partener_id, quantity, tip) VALUES (1, 1, 'F1', 1.0, 'IN'), (2, 1, 'F1', 3.0, 'IN'), (1, 1, 'C1', 1.0, 'OUT');