const express = require('express');
const router = express.Router();
const summariesController = require('../controllers/summariesController');

router.get('/summaries',summariesController.getAllSummaries );

module.exports = router;
