var express = require('express');
var path = require('path');
const session = require('express-session');
var cookieParser = require('cookie-parser');
const fs = require('fs');
const db = require('./database/database');
const schemaPath = path.resolve(__dirname, './database/schema.sql');
const schema = fs.readFileSync(schemaPath, 'utf-8');
const dbPath = path.resolve(__dirname, './database/baza.db');
const bodyParser = require('body-parser');

const homePageRouter = require('./routes/homePage');
const reserveRouter = require('./routes/reserve');
const manageRouter = require('./routes/manage');
const {router: authRouter} = require('./routes/auth');

var app = express();

app.use(express.static(path.join(__dirname, 'public')));


app.use(cookieParser());
app.use(
    session({
      secret: 'your-secret-key',
      resave: false,
      saveUninitialized: false,
      cookie: { secure: false },
    })
);

app.use(bodyParser.urlencoded({ extended: true }));
app.use(express.json());
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

app.use('/', authRouter);
app.use('/homePage', homePageRouter);
app.use('/reserve', reserveRouter);
app.use('/manage', manageRouter);


app.use((req, res, next) => {
    res.status(404).render('error', {
        message: 'Strona nie została znaleziona.',
        error: { status: 404 }
    });
});

app.use((err, req, res) => {
  res.status(err.status || 500);
  res.render('error', { message: err.message, error: err });
});

module.exports = db;

fs.stat(dbPath, () => {

    console.log('Database created');
    db.exec(schema, (err) => {
        if (err) {
            console.error(err.message);
        } else {
            console.log('Data added');
        }
    });

});

module.exports = app;
