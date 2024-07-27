const db = require('../models');
const MeetingParticipant = db.MeetingParticipant;

exports.createParticipant = async (req, res) => {
  try {
    // Lấy dữ liệu từ body của yêu cầu
    const { meeting_id, participant_name, email, role, user_id, attendance_status } = req.body;

    // Log dữ liệu nhận được để kiểm tra
    console.log("Request body:", req.body);

    // Kiểm tra xem Meeting ID và Participant Name có được cung cấp không
    if (!meeting_id || !participant_name) {
      return res.status(400).json({ error: 'Meeting ID and Participant Name are required' });
    }

    // Tạo participant mới trong cơ sở dữ liệu
    const participant = await MeetingParticipant.create({
      meeting_id: meeting_id, // Đảm bảo trường này trong cơ sở dữ liệu là đúng
      participant_name: participant_name, // Đảm bảo trường này trong cơ sở dữ liệu là đúng
      email: email,
      role: role,
      user_id: user_id,
      attendance_status: attendance_status
    });

    // Kiểm tra xem participant có được tạo thành công không
    if (participant) {
      res.status(201).json(participant); // Sử dụng mã trạng thái 201 cho việc tạo tài nguyên thành công
    } else {
      res.status(500).json({ error: 'Failed to create participant' });
    }
    
  } catch (error) {
    console.error('Error creating participant:', error);
    res.status(500).json({ error: 'Internal Server Error' });
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
exports.getParticipantsByMeetingId = async (req, res) => {
  const { meetingId } = req.params;

  try {
    const participants = await MeetingParticipant.findAll({
      where: {
        meeting_id: meetingId
      },
      attributes: [
        'participant_id', 
        'meeting_id', 
        'participant_name', 
        'email', 
        'role', 
        'user_id', 
        'attendance_status', 
        'created_at', 
        'updated_at'
      ]
    });

    if (participants.length > 0) {
      res.json(participants);
    } else {
      res.status(404).json({ error: 'No participants found for the given meeting ID' });
    }
  } catch (error) {
    console.error('Error retrieving participants by meeting ID:', error);
    res.status(500).json({ error: 'Error retrieving participants' });
  }
};

