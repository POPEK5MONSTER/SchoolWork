DROP TABLE IF EXISTS Reservations;
DROP TABLE IF EXISTS Showings;
DROP TABLE IF EXISTS Movies;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Roles;

CREATE TABLE Roles (
                       role_id INTEGER PRIMARY KEY AUTOINCREMENT,
                       description VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE Users (
                       user_id INTEGER NOT NULL CONSTRAINT User_pk PRIMARY KEY AUTOINCREMENT,
                       login VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role_id INTEGER NOT NULL,
                       CONSTRAINT User_Roles_fk FOREIGN KEY (role_id) REFERENCES Roles (role_id)
);

CREATE TABLE Movies (
                        movie_id INTEGER NOT NULL CONSTRAINT Movie_pk PRIMARY KEY AUTOINCREMENT,
                        title VARCHAR(255) NOT NULL,
                        genre VARCHAR(255) NOT NULL,
                        duration INTEGER NOT NULL,
                        description TEXT NOT NULL,
                        release_date DATE NOT NULL
);

CREATE TABLE Showings (
                          showing_id INTEGER NOT NULL CONSTRAINT Showings_pk PRIMARY KEY AUTOINCREMENT,
                          movie_id INTEGER NOT NULL,
                          showing_time DATETIME NOT NULL,
                          CONSTRAINT Showings_Movies_fk FOREIGN KEY (movie_id) REFERENCES Movies (movie_id)
);

CREATE TABLE Reservations (
                              reservation_id INTEGER NOT NULL CONSTRAINT Reservations_pk PRIMARY KEY AUTOINCREMENT,
                              showing_id INTEGER NOT NULL,
                              user_id INTEGER NOT NULL,
                              CONSTRAINT Reservations_Showings_fk FOREIGN KEY (showing_id) REFERENCES Showings (showing_id),
                              CONSTRAINT Reservations_Users_fk FOREIGN KEY (user_id) REFERENCES Users (user_id)
);

INSERT INTO Roles (role_id, description)
VALUES
    (1, 'Admin'),
    (2, 'Customer');

INSERT INTO Users (user_id, login, password, role_id)
VALUES
    (1, 'admin', 'admin123', 1),
    (2, 'james', 'password123', 2),
    (3, 'curry', 'password123', 2);

INSERT INTO Movies (movie_id, title, genre, duration, description, release_date)
VALUES
    (1, 'Incepcja', 'Sci-Fi', 148, 'Złodziej kradnie sekrety poprzez sny.', '2010-07-16'),
    (2, 'Matrix', 'Akcja', 136, 'Haker odkrywa prawdę o rzeczywistości.', '1999-03-31'),
    (3, 'Interstellar', 'Sci-Fi', 169, 'Zespół bada czarną dziurę w kosmosie.', '2014-11-07'),
    (4, 'Titanic', 'Dramat', 195, 'Historia miłości w tle katastrofy statku.', '1997-12-19'),
    (5, 'Władca Pierścieni: Drużyna Pierścienia', 'Fantasy', 178, 'Grupa bohaterów wyrusza na misję, aby zniszczyć pierścień.', '2001-12-19'),
    (6, 'Avatar', 'Sci-Fi', 162, 'Ludzie kolonizują planetę, na której spotykają obcą rasę.', '2009-12-18'),
    (7, 'Pulp Fiction', 'Kryminalny', 154, 'Seria powiązanych ze sobą historii kryminalnych.', '1994-10-14'),
    (8, 'Gladiator', 'Dramat', 155, 'Rzymianin, który walczy, aby zemścić się na zabójcy swojego ojca.', '2000-05-05');

INSERT INTO Showings (showing_id, movie_id, showing_time)
VALUES
    (1, 1, '2024-12-25 18:00:00'),
    (2, 2, '2024-12-25 20:30:00'),
    (3, 3, '2024-12-26 15:00:00'),
    (4, 4, '2024-12-26 20:00:00'),
    (5, 5, '2024-12-27 18:30:00'),
    (6, 6, '2024-12-27 21:00:00'),
    (7, 7, '2024-12-28 20:00:00'),
    (8, 8, '2024-12-28 23:00:00'),
    (9, 1, '2024-12-29 16:00:00'),
    (10, 2, '2024-12-29 19:00:00'),
    (11, 3, '2024-12-30 14:00:00'),
    (12, 4, '2024-12-30 17:00:00'),
    (13, 5, '2025-01-01 20:00:00'),
    (14, 6, '2025-01-01 22:30:00'),
    (15, 7, '2025-01-02 21:00:00'),
    (16, 8, '2025-01-02 18:30:00');

INSERT INTO Reservations (reservation_id, showing_id, user_id)
VALUES
    (1, 1, 2),
    (2, 2, 3),
    (3, 3, 2),
    (4, 4, 3),
    (5, 5, 2);
