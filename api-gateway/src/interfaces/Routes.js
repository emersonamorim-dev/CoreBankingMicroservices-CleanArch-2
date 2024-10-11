const express = require('express');
const { forwardRequest } = require('./Controllers');
const router = express.Router();

router.post('/forward-request', (req, res) => {
  forwardRequest(req, res);
});

module.exports = router;
