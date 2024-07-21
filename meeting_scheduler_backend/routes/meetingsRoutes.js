const express = require('express');
const router = express.Router();
const meetingsController = require('../controllers/meetingsController');

router.get('/meetings',meetingsController.getAllMeetings );

// Route để tạo một cuộc họp mới
router.post('/meetings', meetingsController.createMeeting);

// Route để lấy thông tin một cuộc họp theo ID
router.get('/meetings/:id', meetingsController.getMeetingById);

// Route để cập nhật thông tin một cuộc họp theo ID
router.put('/meetings/:id', meetingsController.updateMeeting);

// Route để xóa một cuộc họp theo ID
router.delete('/meetings/:id', meetingsController.deleteMeeting);

module.exports = router;
