CREATE TABLE IF NOT EXISTS persoane (
    id serial PRIMARY KEY,
    first_name VARCHAR(300) NOT NULL,
    last_name VARCHAR(300) NOT NULL,
    cnp BIGINT NOT NULL,
    phone VARCHAR(15) NOT NULL,
    email VARCHAR(300) NOT NULL
);

INSERT INTO persoane (first_name, last_name, cnp, phone, email) VALUES ('Tudor', 'Coman', '1234567892232', '0712345678', 'tudor@adresa.email'),
                                                                     ('Bill', 'Gates', '4920402343325' , '0787654321', 'bill@gates.com'),
                                                                     ('Sundar', 'Pichai', '9373985624454', '0713579246', 'sundar.pichai@google.com'),
                                                                     ('Elon', 'Musk', '8473878654432', '0714579246', 'elon.musk@tesla.com');