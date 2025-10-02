const express = require('express');
const router = express.Router();
const { isAdmin } = require('./auth');
const db = require('../database/database');

const validateField = (field) => {
    const trimmed = field.trim();
    return trimmed.length > 0 && trimmed.length <= 255;
};
const validateText = (field) => {
    const trimmed = field.trim();
    return trimmed.length > 0;
};
const validateLogin= (field) => {
    const trimmed = field.trim();
    return trimmed.length >= 5 && trimmed.length <= 255;
};
const validatePassword = (field) => {
    const trimmed = field.trim();
    const hasMinTwoDigits = (trimmed.match(/\d/g) || []).length >= 2;
    return trimmed.length >= 5 && trimmed.length <= 255 && hasMinTwoDigits;
};

router.get('/', isAdmin, (req, res) => {
    const queries = {
        Roles: 'SELECT * FROM Roles',
        Users: 'SELECT * FROM Users',
        Movies: 'SELECT * FROM Movies',
        Showings: `
            SELECT Showings.showing_id,
                   Movies.title AS movie_title,
                   Showings.showing_time
            FROM Showings
                     JOIN Movies ON Showings.movie_id = Movies.movie_id
        `,
        Reservations: `
            SELECT Reservations.reservation_id,
                   Movies.title AS movie_title,
                   Users.login  AS user_login
            FROM Reservations
                     JOIN Users ON Reservations.user_id = Users.user_id
                     JOIN Showings ON Reservations.showing_id = Showings.showing_id
                     JOIN Movies ON Movies.movie_id = Showings.movie_id
        `
    };

    const data = {};
    let queryCount = 0;
    const totalQueries = Object.keys(queries).length;

    for (const [key, query] of Object.entries(queries)) {
        db.all(query, [], (err, rows) => {
            if (err){
                console.error(err);
                return res.status(500).send('Błąd serwera.');
            }

            data[key] = rows;
            queryCount++;

            if (queryCount === totalQueries) {
                res.render('manage', {data,  error3 : null,  error4 : null});
            }
        });
    }
});

router.get('/details/Showings/:id', isAdmin, (req, res) => {
    const showingId = req.params.id;

    const showingDetailsQuery = `
        SELECT 
            Showings.showing_id, 
            Showings.showing_time, 
            Movies.title AS movie_title, 
            Movies.genre, 
            Movies.duration, 
            Movies.description, 
            Movies.release_date
        FROM Showings
        JOIN Movies ON Showings.movie_id = Movies.movie_id
        WHERE Showings.showing_id = ?
    `;

    const reservationsQuery = `
        SELECT 
            Users.login AS client_name
        FROM Reservations
        JOIN Users ON Reservations.user_id = Users.user_id
        WHERE Reservations.showing_id = ?
    `;

    db.get(showingDetailsQuery, [showingId], (err, showingDetails) => {
        if (err){
            console.error(err);
            return res.status(500).send('Błąd serwera.');
        }

        db.all(reservationsQuery, [showingId], (err, reservations) => {
            if (err){
                console.error(err);
                return res.status(500).send('Błąd serwera.');
            }

            res.render('details', {
                showing: showingDetails,
                reservations: reservations,
            });
        });
    });
});

router.post('/delete/:table/:id', isAdmin, (req, res) => {
    const { table, id } = req.params;
    let deleteQuery;

    if (table === 'Users') {
        deleteQuery = 'DELETE FROM Users WHERE user_id = ?';
    } else if (table === 'Movies') {
        deleteQuery = 'DELETE FROM Movies WHERE movie_id = ?';
    } else if (table === 'Showings') {
        deleteQuery = 'DELETE FROM Showings WHERE showing_id = ?';
    } else if (table === 'Reservations') {
        deleteQuery = 'DELETE FROM Reservations WHERE reservation_id = ?';
    } else {
        return res.status(400).send('Tabela nieobsługiwana');
    }

    db.run(deleteQuery, [id], (err)=> {
        if (err){
            console.error(err);
            return res.status(500).send('Błąd serwera.');
        }
        res.redirect('/manage');
    });
});

router.get('/edit/Users/:id',  isAdmin, (req, res) => {
    const { id } = req.params;
    const query = 'SELECT * FROM Users WHERE user_id = ?';
    db.get(query, [id], (err, user) => {
        if (err){
            console.error(err);
            return res.status(500).send('Błąd serwera.');
        }
        res.render('edit_user', { user, error1 : null });
    });
});

router.post('/edit/Users/:id',  isAdmin,(req, res) => {
    const { id } = req.params;
    const { login, password, role_id } = req.body;

    if (!validateLogin(login)) {
        return res.status(400).render('edit_user', {
            user: { user_id: id, login, password, role_id },
            error1: 'Login musi mieć co najmniej 6 znaków.'
        });
    }

    if (!validatePassword(password)) {
        return res.status(400).render('edit_user', {
            user: { user_id: id, login, password, role_id },
            error1: 'Hasło musi mieć co najmniej 6 znaków i zawierać co najmniej 2 cyfry.'
        });
    }
    const query = 'UPDATE Users SET login = ?, password = ?, role_id = ? WHERE user_id = ?';

    const checkQuery = `SELECT * FROM Users WHERE login = ?`;
    db.get(checkQuery, [login], (err, row) => {
        if (err) {
            console.error(err);
            return res.status(500).send('Błąd serwera.');
        }
        if (row) {
            return res.status(400).render('edit_user',
                {user: { user_id: id, login, password, role_id },error1: 'Użytkownik o tym loginie już istnieje.'});
        }
            db.run(query, [login, password, role_id, id], (err) => {
                if (err) {
                    console.error(err);
                    return res.status(500).send('Błąd serwera.');
                }
                res.redirect('/manage');
            });
    });
});

router.get('/edit/Movies/:id', isAdmin,  (req, res) => {
    const { id } = req.params;
    const query = 'SELECT * FROM Movies WHERE movie_id = ?';
    db.get(query, [id], (err, movie) => {
        if (err){
            console.error(err);
            return res.status(500).send('Błąd serwera.');
        }
        res.render('edit_movie.ejs', { movie, error2: null });
    });
});

router.post('/edit/Movies/:id',  isAdmin,(req, res) => {
    const { id } = req.params;
    const { title, genre, duration, description, release_date } = req.body;

    if (!validateText(description)) {
        return res.render('edit_movie', {
            movie: { movie_id: id, title, genre, duration, description, release_date },
            error2: 'Text musi mieć co najmniej 1 znak.'
        });
    }

    if (![title,genre].every(validateField)) {
        return res.render('edit_movie', {
            movie: { movie_id: id, title, genre, duration, description, release_date },
            error2: 'Text musi mieć co najmniej od 1 - 255 znaków.'
        });
    }
    const query = 'UPDATE Movies SET title = ?, genre = ?, duration = ?, description = ?, release_date = ? WHERE movie_id = ?';
    db.run(query, [title, genre, duration, description, release_date, id],  (err)=> {
        if (err){
            console.error(err);
            return res.status(500).send('Błąd serwera.');
        }
        res.redirect('/manage');
    });
});

router.get('/edit/Showings/:id', isAdmin, (req, res) => {
    const showingQuery = `SELECT Showings.showing_id, Movies.title AS movie_title, Showings.showing_time, Showings.movie_id
                          FROM Showings
                            JOIN Movies ON Showings.movie_id = Movies.movie_id
                          WHERE Showings.showing_id = ?`;

    const moviesQuery = 'SELECT * FROM Movies';


    db.get(showingQuery, [req.params.id], (err, showing) => {
        if (err){
            console.error(err);
            return res.status(500).send('Błąd serwera.');
        }

        db.all(moviesQuery, [], (err, movies) => {
            if (err){
                console.error(err);
                return res.status(500).send('Błąd serwera.');
            }
            res.render('edit_showing', { showing, movies });
        });
    });
});

router.post('/edit/Showings/:id', isAdmin, (req, res) => {
    const { id } = req.params;
    const { movie_id, showing_time } = req.body;

    const query = 'UPDATE Showings SET movie_id = ?, showing_time = ? WHERE showing_id = ?';
    db.run(query, [movie_id, showing_time, id],  (err) =>{
        if (err){
            console.error(err);
            return res.status(500).send('Błąd serwera.');
        }
        res.redirect('/manage');
    });
});

router.get('/edit/Reservations/:id',  isAdmin,(req, res) => {
    const reservationQuery = `SELECT Reservations.reservation_id, Reservations.user_id, Reservations.showing_id
                              FROM Reservations
                              WHERE reservation_id = ?`;

    const usersQuery = 'SELECT * FROM Users';
    const showingsQuery = `SELECT Showings.showing_id, Movies.title AS movie_title, Showings.showing_time
                           FROM Showings
                           JOIN Movies ON Showings.movie_id = Movies.movie_id`;

    db.get(reservationQuery, [req.params.id], (err, reservation) => {
        if (err){
            console.error(err);
            return res.status(500).send('Błąd serwera.');
        }

        db.all(usersQuery, [], (err, users) => {
            if (err){
                console.error(err);
                return res.status(500).send('Błąd serwera.');
            }

            db.all(showingsQuery, [], (err, showings) => {
                if (err){
                    console.error(err);
                    return res.status(500).send('Błąd serwera.');
                }

                res.render('edit_reservation', { reservation, users, showings });
            });
        });
    });
});

router.post('/edit/Reservations/:id', isAdmin, (req, res) => {
    const { id } = req.params;
    const { user_id, showing_id } = req.body;

    const query = 'UPDATE Reservations SET user_id = ?, showing_id = ? WHERE reservation_id = ?';
    db.run(query, [user_id, showing_id, id],  (err) =>{
        if (err){
            console.error(err);
            return res.status(500).send('Błąd serwera.');
        }
        res.redirect('/manage');
    });
});

router.post('/add/Users',  isAdmin,(req, res) => {
    const { login, password, role_id } = req.body;

    const query = `INSERT INTO Users (login, password, role_id)
                   VALUES (?, ?, ?)`;

    const checkQuery = `SELECT * FROM Users WHERE login = ?`;
    db.get(checkQuery, [login], (err, row) => {
        if (err) {
            console.error(err);
            return res.status(500).send('Błąd serwera.');
        }

        if (row) {
            return res.status(400).send('Użytkownik o tym loginie już istnieje.');
        }
            db.run(query, [login, password, role_id], (err) => {
                if (err) {
                    console.error(err);
                    return res.status(500).send('Błąd serwera.');
                } else {
                    res.redirect('/manage');
                }
            });
    });
});

router.post('/add/Movies',  isAdmin,(req, res) => {
    const { title, genre, duration, description, release_date } = req.body;

    const query = `INSERT INTO Movies (title, genre, duration, description, release_date)
                   VALUES (?, ?, ?, ?, ?)`;

    db.run(query, [title, genre, duration, description, release_date], (err)=> {
        if (err){
            console.error(err);
            return res.status(500).send('Błąd serwera.');
        } else {
            res.redirect('/manage');
        }
    });
});

router.post('/add/Showings',  isAdmin,(req, res) => {
    const { movie_id, showing_time } = req.body;

    const query = `INSERT INTO Showings (movie_id, showing_time)
                   VALUES (?, ?)`;

    db.run(query, [movie_id, showing_time], (err) =>{
        if (err){
            console.error(err);
            return res.status(500).send('Błąd serwera.');
        } else {
            res.redirect('/manage');         }
    });
});

router.post('/add/Reservations', isAdmin,(req, res) => {
    const { showing_id, user_id } = req.body;

    const query = `INSERT INTO Reservations (showing_id, user_id)
                   VALUES (?, ?)`;

    db.run(query, [showing_id, user_id], (err)=> {
        if (err){
            console.error(err);
            return res.status(500).send('Błąd serwera.');
        } else {
            res.redirect('/manage');
        }
    });
});

module.exports = router;
