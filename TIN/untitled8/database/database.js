const sqlite3 = require('sqlite3').verbose();
const path = require('path');

const dbPath = path.resolve(__dirname, 'baza.db');

const db = new sqlite3.Database(dbPath, (err) => {
    if (err) {
        console.error('Error database', err.message);
    } else {
        console.log('Connected to database.');
    }
});

module.exports = db;