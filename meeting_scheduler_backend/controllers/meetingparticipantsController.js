const db = require('../models');
const MeetingParticipant = db.MeetingParticipant;

exports.createParticipant = async (req, res) => {
  try {
    const { meetingId, participantName, email, role, userId, attendanceStatus } = req.body;

    // Kiểm tra dữ liệu nhận được từ client
    if (!meetingId || !participantName) {
      return res.status(400).json({ error: 'Meeting ID and Participant Name are required' });
    }

    console.log("Request body:", req.body);

    const participant = await MeetingParticipant.create({
      meeting_id: meetingId,
      participant_name: participantName,
      email: email,
      role: role,
      user_id: userId,
      attendance_status: attendanceStatus
    });

    if (participant) {
      res.json(participant);
    } else {
      return res.status(404).json({ error: 'Create participant failed' });
    }

  } catch (error) {
    console.error('Error creating participant:', error);
    res.status(500).json({ error: 'Create participant failed' });
  }
};


exports.getAllParticipants = async (req, res) => {
  try {
    const participants = await MeetingParticipant.findAll();
    res.json(participants);
  } catch (error) {
    res.status(500).json({ error: 'Get all participants failed' });
  }
};

exports.getParticipantById = async (req, res) => {
  try {
    const participant = await MeetingParticipant.findByPk(req.params.id);
    if (participant) {
      res.json(participant);
    } else {
      res.status(404).json({ error: 'Participant not found' });
    }
  } catch (error) {
    res.status(500).json({ error: 'Get participant by ID failed' });
  }
};

exports.updateParticipant = async (req, res) => {
  try {
    const participant = await MeetingParticipant.findByPk(req.params.id);
    if (participant) {
      await participant.update(req.body);
      res.json(participant);
    } else {
      res.status(404).json({ error: 'Participant not found' });
    }
  } catch (error) {
    res.status(500).json({ error: 'Update participant failed' });
  }
};

exports.deleteParticipant = async (req, res) => {
  try {
    const participant = await MeetingParticipant.findByPk(req.params.id);
    if (participant) {
      await participant.destroy();
      res.json({ message: 'Participant deleted' });
    } else {
      res.status(404).json({ error: 'Participant not found' });
    }
  } catch (error) {
    res.status(500).json({ error: 'Delete participant failed' });
  }
};

exports.getParticipantRole = async (req, res) => {
  const userId = req.params.userId;

  try {
    // Tìm participant có userId và lấy vai trò (role)
    const participant = await MeetingParticipant.findOne({
      where: {
        user_id: userId
      }
    });

    if (participant) {
      res.json({ role: participant.role });
    } else {
      res.status(404).json({ error: 'Participant not found' });
    }
  } catch (error) {
    console.error('Error retrieving participant role:', error);
    res.status(500).json({ error: 'Server error' });
  }
};
