const express = require('express');
const router = express.Router();
const DiscoveryController = require('../interfaces/DiscoveryController');

router.post('/discovery/register', (req, res) => {
  DiscoveryController.registerService(req, res);
});

router.get('/discovery/services', (req, res) => {
  DiscoveryController.getAllServices(req, res);
});

router.delete('/discovery/remove', (req, res) => {
  DiscoveryController.removeService(req, res);
});

module.exports = router;
