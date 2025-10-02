const express = require('express');
const db = require('../database/database');
const router = express.Router();

function isAuthenticated(req, res, next) {
    if (req.session && req.session.user) {
        return next();
    }
    res.redirect('/login');
}
function isAdmin(req, res, next) {
    if (req.session && req.session.user && req.session.user.role_id === 1) {
        return next();
    }
    res.redirect('/login');
}
function isClient(req, res, next) {
    if (req.session && req.session.user && req.session.user.role_id === 2) {
        return next();
    }
    res.redirect('/login');
}
const validateLogin= (field) => {
    const trimmed = field.trim();
    return trimmed.length >= 5 && trimmed.length <= 255;
};
const validatePassword = (field) => {
    const trimmed = field.trim();
    const hasMinTwoDigits = (trimmed.match(/\d/g) || []).length >= 2;
    return trimmed.length >= 5 && trimmed.length <= 255 && hasMinTwoDigits;
};

router.get('/login', (req, res) => {
    res.render('login', { error: null });
});

router.post('/login', (req, res) => {
    const { login, password } = req.body;

    const query = `SELECT * FROM Users WHERE login = ?`;
    db.get(query, [login], (err, user) => {
        if (err){
            console.error(err);
            return res.status(500).send('Błąd serwera.');
        }

        if (!user || user.password !== password) {
            return res.status(400).render('login', { error: 'Niepoprawne login lub hasło', login: login});
        }

        req.session.user = { user_id: user.user_id, role_id: user.role_id };

        res.redirect('/homePage');
    });
});

router.get('/register', (req, res) => {
    res.render('register', { error: null });
});

router.post('/register', (req, res) => {
    const { login, password, password2 } = req.body;

    if (!validateLogin(login)) {
        return res.status(400).render('register', { error: 'Login musi mieć minimum 5 znaków' ,login: login});
    }
    if (!validatePassword(password)) {
        return res.status(400).render('register', { error: 'Hasło musi mieć minimum 5 znaków i 2 liczby',login: login });
    }

    if (password !== password2) {
        return res.status(400).render('register', { error: 'Hasła muszą być takie same!',login: login });
    }
    const role_id = 2;

    const checkUserQuery = `SELECT * FROM Users WHERE login = ?`;
    db.get(checkUserQuery, [login], (err, user) => {
        if (err) {
            console.error(err);
            return res.status(500).send('Błąd serwera.');
        }
        if (user) {
            return res.status(400).render('register', { error: 'Login już istnieje.' ,login: login});
        }

        const query = `INSERT INTO Users (login, password, role_id) VALUES (?, ?, ?)`;
        db.run(query, [login, password, role_id], (err) => {
            if (err) {
                console.error(err);
                return res.status(500).send('Błąd serwera.');
            }

            res.redirect('/login');
        });
    });
});

router.get('/logout', (req, res) => {
    req.session.destroy((err) => {
        if (err) {
            console.error(err);
            return res.status(500).send('Błąd serwera.');
        }

        res.redirect('/login');
    });
});

module.exports = {router, isAuthenticated, isAdmin, isClient};
