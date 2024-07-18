const express = require('express');
const router = express.Router();
const meetingresultsController = require('../controllers/meetingresultsController');

router.get('/meetingresults',meetingresultsController.getAllMeetingResults);

module.exports = router;
