const express = require('express');
const router = express.Router();
const participantsController = require('../controllers/meetingparticipantsController');

router.get('/meetingparticipants',participantsController.getAllParticipants );
router.post('/meetingparticipants',participantsController.createParticipant );

module.exports = router;
