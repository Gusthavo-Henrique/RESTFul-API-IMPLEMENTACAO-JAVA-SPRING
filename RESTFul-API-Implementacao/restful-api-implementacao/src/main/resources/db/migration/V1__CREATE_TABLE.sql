CREATE TABLE books
(
    id serial,
    author VARCHAR(255),
    name VARCHAR(255),
    price double precision NOT NULL,
    CONSTRAINT books_pkey PRIMARY KEY (id)
);
