DROP TABLE MA CASCADE CONSTRAINTS;
DROP TABLE Okupa CASCADE CONSTRAINTS;
DROP TABLE Jugador CASCADE CONSTRAINTS;
DROP TABLE Partida CASCADE CONSTRAINTS;
DROP TABLE Usuari CASCADE CONSTRAINTS;
DROP TABLE Carta CASCADE CONSTRAINTS;
DROP TABLE TipusCarta CASCADE CONSTRAINTS;
DROP TABLE Frontera CASCADE CONSTRAINTS;
DROP TABLE Pais CASCADE CONSTRAINTS;
DROP TABLE Continent CASCADE CONSTRAINTS;
DROP TABLE Estats CASCADE CONSTRAINTS;

CREATE TABLE Continent (
    id NUMBER(19) PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    reforc_tropes INT NOT NULL
);

CREATE TABLE Pais (
    id NUMBER(19) PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    continent_id NUMBER(19) NOT NULL,
    FOREIGN KEY (continent_id) REFERENCES Continent(id)
);

CREATE TABLE Frontera (
    pais1_id NUMBER(19) NOT NULL,
    pais2_id NUMBER(19) NOT NULL,
    PRIMARY KEY (pais1_id, pais2_id),
    FOREIGN KEY (pais1_id) REFERENCES Pais(id),
    FOREIGN KEY (pais2_id) REFERENCES Pais(id)
);

CREATE TABLE TipusCarta (
    id NUMBER(19) PRIMARY KEY,
    nom VARCHAR(255) NOT NULL
);

CREATE TABLE Carta (
    id NUMBER(19) PRIMARY KEY,
    tipus NUMBER(19) NOT NULL,
    pais_id NUMBER(19),
	FOREIGN KEY (pais_id) REFERENCES Pais(id),
    FOREIGN KEY (tipus) REFERENCES TipusCarta(id)
);

CREATE TABLE Estats (
    id NUMBER(19) PRIMARY KEY,
    nom VARCHAR(255) NOT NULL
);

CREATE TABLE Usuari (
    id NUMBER(19) PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    login VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    avatar VARCHAR(255) NOT NULL,
    wins INT NOT NULL,
    games INT NOT NULL
);

CREATE TABLE Partida (
    id NUMBER(19) PRIMARY KEY,
    data_inici DATE NOT NULL,
    nom VARCHAR(255) NOT NULL,
    token VARCHAR(255),
    max_players INT NOT NULL,
    admin_id NUMBER(19) NOT NULL,
    torn_player_id NUMBER(19) NOT NULL,
    estat_torn NUMBER(19) NOT NULL,
	CONSTRAINT partida_token_uk UNIQUE (token),
    FOREIGN KEY (estat_torn) REFERENCES Estats(id)
);

CREATE TABLE Jugador (
    id NUMBER(19) PRIMARY KEY,
    skfUser_id NUMBER(19) NOT NULL,
    skfPartida_id NUMBER(19) NOT NULL,
    skfNumero INT NOT NULL,
    FOREIGN KEY (skfUser_id) REFERENCES Usuaris(id),
    FOREIGN KEY (skfPartida_id) REFERENCES Partida(id),
    UNIQUE (skfUser_id, skfPartida_id),
    UNIQUE (skfPartida_id, skfNumero)
);

ALTER TABLE Partida ADD CONSTRAINT fk_admin_id FOREIGN KEY (admin_id) REFERENCES Jugador(id);
ALTER TABLE Partida ADD CONSTRAINT fk_torn_player_id FOREIGN KEY (torn_player_id) REFERENCES Jugador(id);

CREATE TABLE Okupa (
    pais_id NUMBER(19) NOT NULL,
    player_id NUMBER(19) NOT NULL,
    tropes INT NOT NULL,
    PRIMARY KEY (pais_id, player_id),
    FOREIGN KEY (pais_id) REFERENCES Pais(id),
    FOREIGN KEY (player_id) REFERENCES Jugador(id)
);

CREATE TABLE MA (
    carta_id NUMBER(19) NOT NULL,
    jugador_id NUMBER(19) NOT NULL,
    PRIMARY KEY (carta_id, jugador_id),
    FOREIGN KEY (carta_id) REFERENCES Carta(id),
    FOREIGN KEY (jugador_id) REFERENCES Jugador(id)
);

INSERT INTO TipusCarta (id, nom) VALUES( 0, 'Comodí');
INSERT INTO TipusCarta (id, nom) VALUES( 1, 'Infanteria');
INSERT INTO TipusCarta (id, nom) VALUES( 2, 'Cavalleria');
INSERT INTO TipusCarta (id, nom) VALUES( 3, 'Artilleria');

INSERT INTO Estats (id, nom) VALUES (0, 'Espera');
INSERT INTO Estats (id, nom) VALUES (1, 'Col·locació inicial');
INSERT INTO Estats (id, nom) VALUES (2, 'Reforçar pais');
INSERT INTO Estats (id, nom) VALUES (3, 'Assignar tropes');
INSERT INTO Estats (id, nom) VALUES (4, 'Atac');
INSERT INTO Estats (id, nom) VALUES (5, 'Fortificació');
INSERT INTO Estats (id, nom) VALUES (6, 'Final');

INSERT INTO Continent (id, nom, reforc_tropes) VALUES (1, 'Amèrica del Nord', 5);
INSERT INTO Continent (id, nom, reforc_tropes) VALUES (2, 'Amèrica del Sud', 2);
INSERT INTO Continent (id, nom, reforc_tropes) VALUES (3, 'Europa', 5);
INSERT INTO Continent (id, nom, reforc_tropes) VALUES (4, 'Àfrica', 3);
INSERT INTO Continent (id, nom, reforc_tropes) VALUES (5, 'Asia', 7);
INSERT INTO Continent (id, nom, reforc_tropes) VALUES (6, 'Australia', 2);

INSERT INTO Pais (id, nom, continent_id) VALUES (1, 'Alaska', 1);
INSERT INTO Pais (id, nom, continent_id) VALUES (2, 'Territori del Nord-oest', 1);
INSERT INTO Pais (id, nom, continent_id) VALUES (3, 'Alberta', 1);
INSERT INTO Pais (id, nom, continent_id) VALUES (4, 'Ontario', 1);
INSERT INTO Pais (id, nom, continent_id) VALUES (5, 'Quebec', 1);
INSERT INTO Pais (id, nom, continent_id) VALUES (6, 'Oest dels Estats Units', 1);
INSERT INTO Pais (id, nom, continent_id) VALUES (7, 'Est dels Estats Units', 1);
INSERT INTO Pais (id, nom, continent_id) VALUES (8, 'Amèrica Central', 1);
INSERT INTO Pais (id, nom, continent_id) VALUES (9, 'Grenlàndia', 1);
INSERT INTO Pais (id, nom, continent_id) VALUES (10, 'Veneçuela', 2);
INSERT INTO Pais (id, nom, continent_id) VALUES (11, 'Perú', 2);
INSERT INTO Pais (id, nom, continent_id) VALUES (12, 'Brasil', 2);
INSERT INTO Pais (id, nom, continent_id) VALUES (13, 'Argentina', 2);
INSERT INTO Pais (id, nom, continent_id) VALUES (14, 'Islàndia', 3);
INSERT INTO Pais (id, nom, continent_id) VALUES (15, 'Gran Bretanya', 3);
INSERT INTO Pais (id, nom, continent_id) VALUES (16, 'Escandinàvia', 3);
INSERT INTO Pais (id, nom, continent_id) VALUES (17, 'Europa del Nord', 3);
INSERT INTO Pais (id, nom, continent_id) VALUES (18, 'Europa Occidental', 3);
INSERT INTO Pais (id, nom, continent_id) VALUES (19, 'Europa Meridional', 3);
INSERT INTO Pais (id, nom, continent_id) VALUES (20, 'Ucraïna', 3);
INSERT INTO Pais (id, nom, continent_id) VALUES (21, 'Àfrica del Nord', 4);
INSERT INTO Pais (id, nom, continent_id) VALUES (22, 'Egipte', 4);
INSERT INTO Pais (id, nom, continent_id) VALUES (23, 'Àfrica Oriental', 4);
INSERT INTO Pais (id, nom, continent_id) VALUES (24, 'Congo', 4);
INSERT INTO Pais (id, nom, continent_id) VALUES (25, 'Sud-àfrica', 4);
INSERT INTO Pais (id, nom, continent_id) VALUES (26, 'Madagascar', 4);
INSERT INTO Pais (id, nom, continent_id) VALUES (27, 'Ural', 5);
INSERT INTO Pais (id, nom, continent_id) VALUES (28, 'Sibèria', 5);
INSERT INTO Pais (id, nom, continent_id) VALUES (29, 'Iakutsk', 5);
INSERT INTO Pais (id, nom, continent_id) VALUES (30, 'Kamtxatka', 5);
INSERT INTO Pais (id, nom, continent_id) VALUES (31, 'Irkutsk', 5);
INSERT INTO Pais (id, nom, continent_id) VALUES (32, 'Mongòlia', 5);
INSERT INTO Pais (id, nom, continent_id) VALUES (33, 'Japó', 5);
INSERT INTO Pais (id, nom, continent_id) VALUES (34, 'Afganistan', 5);
INSERT INTO Pais (id, nom, continent_id) VALUES (35, 'Xina', 5);
INSERT INTO Pais (id, nom, continent_id) VALUES (36, 'Pròxim Orient', 5);
INSERT INTO Pais (id, nom, continent_id) VALUES (37, 'Índia', 5);
INSERT INTO Pais (id, nom, continent_id) VALUES (38, 'Sud-est Asiàtic', 5);
INSERT INTO Pais (id, nom, continent_id) VALUES (39, 'Indonèsia', 6);
INSERT INTO Pais (id, nom, continent_id) VALUES (40, 'Nova Guinea', 6);
INSERT INTO Pais (id, nom, continent_id) VALUES (41, 'Austràlia Occidental', 6);
INSERT INTO Pais (id, nom, continent_id) VALUES (42, 'Austràlia Oriental', 6);

INSERT INTO Frontera (pais1_id, pais2_id) VALUES (1, 2);    
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (1, 3);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (1, 30);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (2, 3);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (2, 4);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (2, 9);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (3, 4);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (3, 6);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (4, 5);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (4, 6);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (4, 7);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (5, 7);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (5, 9);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (6, 7);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (6, 8);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (7, 8);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (8, 10);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (9, 14);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (9, 2);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (9, 4);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (9, 5);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (10, 11);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (10, 12);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (11, 12);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (11, 13);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (12, 13);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (14, 15);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (14, 16);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (15, 16);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (15, 17);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (15, 18);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (16, 17);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (16, 20);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (17, 18);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (17, 19);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (17, 20);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (18, 19);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (19, 20);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (19, 22);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (19, 36);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (20, 27);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (20, 34);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (21, 22);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (21, 23);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (21, 24);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (21, 12);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (21, 18);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (22, 23);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (22, 36);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (23, 24);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (23, 25);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (23, 26);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (24, 25);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (25, 26);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (27, 28);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (27, 31);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (27, 34);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (28, 29);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (28, 31);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (29, 30);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (30, 31);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (30, 32);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (30, 33);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (31, 32);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (32, 33);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (32, 35);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (34, 35);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (34, 36);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (34, 37);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (35, 37);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (35, 38);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (36, 37);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (37, 38);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (38, 39);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (39, 40);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (39, 41);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (40, 42);
INSERT INTO Frontera (pais1_id, pais2_id) VALUES (41, 42);

--For testing!
INSERT INTO Usuari (id, nom, login, password, avatar, wins, games) VALUES (1, 'Andreu Niso', 'aniso', 'aniso', 'avatar_home_1.png', 1000, 1000);
INSERT INTO Usuari (id, nom, login, password, avatar, wins, games) VALUES (2, 'Elena Romeu', 'eromeu', 'eromeu', 'avatar_dona_1.png', 1000, 1000);
INSERT INTO Usuari (id, nom, login, password, avatar, wins, games) VALUES (3, 'Ferran Morgades', 'fmorgades', 'fmorgades', 'avatar_home_2.png', 1000, 1000);

COMMIT;