const express = require('express');
const db = require('../database/database');
const { isClient } = require('./auth');
const router = express.Router();

router.get('/', (req, res) => {
    const searchDate = req.query.searchDate;
    const page = parseInt(req.query.page) || 1;
    const limit = 7;
    const view = (page - 1) * limit;

    let query = `
        SELECT Showings.showing_id,
               Showings.showing_time,
               Movies.title AS movie_title
        FROM Showings
                 JOIN Movies ON Showings.movie_id = Movies.movie_id
    `;
    let countQuery = `
        SELECT COUNT(*) AS totalCount
        FROM Showings
                 JOIN Movies ON Showings.movie_id = Movies.movie_id
    `;

    const params = [];
    const countParams = [];

    if (searchDate) {
        query += ` WHERE DATE(Showings.showing_time) = ?`;
        countQuery += ` WHERE DATE(Showings.showing_time) = ?`;
        params.push(searchDate);
        countParams.push(searchDate);
    }

    query += ` LIMIT ? OFFSET ?`;
    params.push(limit, view);

    db.all(countQuery, countParams, (err, countRows) => {
        if (err) {
            console.error(err);
            return res.status(500).send('Błąd serwera.');
        }

        const totalCount = countRows[0].totalCount;
        const totalPages = Math.ceil(totalCount / limit);

        db.all(query, params, (err, rows) => {
            if (err) {
                console.error(err);
                return res.status(500).send('Błąd serwera.');
            }

            res.render('reserve', {
                showings: rows,
                currentPage: page,
                totalPages: totalPages,
                searchDate: searchDate || '',
            });
        });
    });
});

router.get('/:showingId', (req, res) => {
    const showingId = req.params.showingId;

    const query = `
        SELECT
            Showings.showing_id,
            Movies.title AS movie_title,
            Movies.genre,
            Movies.duration,
            Movies.description,
            Movies.release_date,
            Showings.showing_time
        FROM Showings
                 JOIN Movies ON Showings.movie_id = Movies.movie_id
        WHERE Showings.showing_id = ?
    `;

    db.get(query, [showingId], (err, row) => {
        if (err) {
            res.status(500).render('error', { message: 'Błąd serwera.', error: err });
        } else if (!row) {
            res.status(404).render('error', { message: 'Nie znaleziono seansu'});
        } else {
            res.render('showingDetails', { showing: row , user: req.session.user });
        }
    });
});

router.post('/:showingId/book', isClient, (req, res) => {
    const showingId = req.params.showingId;
    const userId = req.session.user.user_id;

    const query = `
        INSERT INTO Reservations (showing_id, user_id)
        VALUES (?, ?)
    `;

    db.run(query, [showingId, userId],  (err) => {
        if (err) {
            console.error(err);
            return res.status(500).send('Błąd serwera.');
        }
        res.redirect(`/reserve`);
    });
});


module.exports = router;
