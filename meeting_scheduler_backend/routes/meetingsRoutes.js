const express = require('express');
const router = express.Router();
const meetingsController = require('../controllers/meetingsController');

router.get('/meetings',meetingsController.getAllMeetings );

// Route để tạo một cuộc họp mới
router.post('/meetings', meetingsController.createMeeting);

router.post('/meetings-with-files', meetingsController.createMeetingWithFiles);

// Route để lấy thông tin một cuộc họp theo ID
router.get('/meetings/:id', meetingsController.getMeetingById);

// Route để cập nhật thông tin một cuộc họp theo ID
router.put('/meetings/:id', meetingsController.updateMeeting);

// Route để xóa một cuộc họp theo ID
router.delete('/meetings/:id', meetingsController.deleteMeeting);

router.get('/download/:filename', meetingsController.downloadFile);

module.exports = router;
