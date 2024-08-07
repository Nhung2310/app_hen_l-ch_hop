const express = require('express');
const router = express.Router();
const summariesController = require('../controllers/summariesController');

router.get('/summaries',summariesController.getAllSummaries );
// Route để lấy các tóm tắt theo meeting_id
router.get('/summaries-by-meeting-id/:meeting_id', summariesController.getSummariesByMeetingId);

// Route để tạo tóm tắt
router.post('/summaries', summariesController.createSummary);
module.exports = router;
