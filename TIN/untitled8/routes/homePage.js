const express = require('express');
const {isAuthenticated} = require("./auth");
const router = express.Router();

router.get('/', isAuthenticated,(req, res) => {
  res.render('homePage',{ user: req.session.user });
});

module.exports = router;
