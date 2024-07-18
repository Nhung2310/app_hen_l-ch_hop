const express = require('express');
const router = express.Router();
const meetingsController = require('../controllers/meetingsController');

router.get('/meetings',meetingsController.getAllMeetings );

module.exports = router;
