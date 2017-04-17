CREATE TABLE usuario
(
    _id integer NOT NULL PRIMARY KEY AUTOINCREMENT,
    login varchar(255) NOT NULL,
    senha varchar(255) NOT NULL
);

CREATE TABLE categoriadespesa
(
    _id integer NOT NULL PRIMARY KEY AUTOINCREMENT,
    nome varchar(255) NOT NULL
);

CREATE TABLE despesa
(
    _id integer NOT NULL PRIMARY KEY AUTOINCREMENT,
    categoriadespesa_id integer NOT NULL,
    nome varchar(255) NOT NULL,
    data varchar(255) NOT NULL,
    valor real NOT NULL,
    FOREIGN KEY (categoriadespesa_id)
    REFERENCES categoriadespesa(_id)
);
