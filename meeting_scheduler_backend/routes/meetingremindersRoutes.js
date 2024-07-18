const express = require('express');
const router = express.Router();
const meetingremindersController = require('../controllers/meetingremindersController');

router.get('/meetingreminders', meetingremindersController.getAllMeetingReminders);

module.exports = router;
