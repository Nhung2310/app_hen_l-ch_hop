const express = require('express');
const router = express.Router();
const participantsController = require('../controllers/participantsController');

router.get('/participants',participantsController.getAllParticipants );
router.get('/participants',participantsController.getParticipantRole );

module.exports = router;
