const express = require('express');
const router = express.Router();
const participantsController = require('../controllers/participantsController');

router.get('/meetings',participantsController.getAllParticipants );

module.exports = router;
