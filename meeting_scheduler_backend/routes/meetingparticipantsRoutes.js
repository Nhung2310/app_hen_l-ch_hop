const express = require('express');
const router = express.Router();
const participantsController = require('../controllers/meetingparticipantsController');

router.get('/meetingparticipants',participantsController.getAllParticipants );
router.post('/meetingparticipants',participantsController.createParticipant );
// Route để lấy danh sách người tham gia dựa trên meeting_id
router.get('/meetingparticipants/:meetingId', participantsController.getParticipantsByMeetingId);
// Endpoint để lấy danh sách meeting ID dựa trên user ID
router.get('/meeting-ids/:userId', participantsController.getMeetingIdsByUserId);


module.exports = router;
