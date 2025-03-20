DROP TABLE IF EXISTS authors CASCADE;
DROP TABLE IF EXISTS books CASCADE;
DROP TABLE IF EXISTS books_authors CASCADE;
DROP TABLE IF EXISTS magazines CASCADE;
DROP TABLE IF EXISTS publishers CASCADE;

CREATE TABLE IF NOT EXISTS authors (
	id serial NOT NULL,
	name varchar(100) NOT NULL,
	CONSTRAINT authors_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS books (
	id serial NOT NULL,
	title varchar(200) NOT NULL,
	quantity integer NOT NULL,
	CONSTRAINT books_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS books_authors (
	id serial NOT NULL,
	book_id integer,
	author_id integer,
	CONSTRAINT booksauthors_pkey PRIMARY KEY (id),
	CONSTRAINT fk_authors_id FOREIGN KEY (author_id)
		REFERENCES authors (id) MATCH SIMPLE
		ON UPDATE CASCADE
		ON DELETE CASCADE
		NOT VALID,
	CONSTRAINT fk_books_id FOREIGN KEY (book_id)
		REFERENCES books (id) MATCH SIMPLE
		ON UPDATE CASCADE
		ON DELETE CASCADE
		NOT VALID
);

CREATE TABLE IF NOT EXISTS publishers (
	id serial NOT NULL,
	name varchar(100) NOT NULL,
	CONSTRAINT publishers_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS magazines (
	id serial NOT NULL,
	title varchar(200) NOT NULL,
	quantity integer NOT NULL,
	publisher_id integer,
	CONSTRAINT magazines_pkey PRIMARY KEY (id),
	CONSTRAINT fk_publishers_id FOREIGN KEY (publisher_id)
		REFERENCES publishers (id) MATCH SIMPLE
		ON UPDATE CASCADE
		ON DELETE SET NULL
		NOT VALID
);

INSERT INTO authors (name)
VALUES ('Herbert Schildt'),
       ('Erich Gamma'),
       ('Richard Helm'),
       ('Ralph Johnson'),
       ('John Vlissides'),
       ('Aditya Y. Bhargava'),
       ('Leo Tolstoy'),
       ('Mark Twain'),
       ('Frank Herbert');

INSERT INTO books (title, quantity)
VALUES ('Java The Complete Reference 9th Edition', 40),
       ('Design Patterns: Elements of Reusable Object-Oriented Software', 30),
       ('Grokking Algorithms', 25),
       ('War and Peace', 30),
       ('Adventures of Huckleberry Finn', 50),
       ('Dune', 45);

INSERT INTO books_authors (book_id, author_id)
VALUES (1, 1),
       (2, 2),
       (2, 3),
       (2, 4),
       (2, 5),
       (3, 6),
       (4, 7),
       (5, 8),
       (6, 9);

INSERT INTO publishers (name)
VALUES ('National Geographic Society'),
       ('The National Trust'),
       ('Immediate Media Company');

INSERT INTO magazines (title, quantity, publisher_id)
VALUES ('National Geographic', 50, 1),
       ('National Trust Magazine', 40, 2),
       ('Radio Times', 25, 3),
       ('BBC Good Food', 20, 3),
       ('BBC Gardeners'' World', 20, 3),
       ('BBC Top Gear Magazine', 20, 3);
