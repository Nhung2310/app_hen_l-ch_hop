const db = require('../models');
const Meeting = db.Meeting;

exports.createMeeting = async (req, res) => {
  try {
    const meeting = await Meeting.create(req.body);

    if (meeting) {
      res.json(meeting);
    } else {
      return res.status(404).json({ error: 'Create meeting failed' });
    }

  } catch (error) {
    res.status(500).json({ error: 'Create meeting failed' });
  }
};

exports.getAllMeetings = async (req, res) => {
  try {
    const meetings = await Meeting.findAll();
    res.json(meetings);
  } catch (error) {
    res.status(500).json({ error: 'Get all meetings failed' });
  }
};

exports.getMeetingById = async (req, res) => {
  try {
    const meeting = await Meeting.findByPk(req.params.id);
    if (meeting) {
      res.json(meeting);
    } else {
      res.status(404).json({ error: 'Meeting not found' });
    }
  } catch (error) {
    res.status(500).json({ error: 'Get meeting by ID failed' });
  }
};

exports.updateMeeting = async (req, res) => {
  try {
    const meeting = await Meeting.findByPk(req.params.id);
    if (meeting) {
      await meeting.update(req.body);
      res.json(meeting);
    } else {
      res.status(404).json({ error: 'Meeting not found' });
    }
  } catch (error) {
    res.status(500).json({ error: 'Update meeting failed' });
  }
};

exports.deleteMeeting = async (req, res) => {
  try {
    const meeting = await Meeting.findByPk(req.params.id);
    if (meeting) {
      await meeting.destroy();
      res.json({ message: 'Meeting deleted' });
    } else {
      res.status(404).json({ error: 'Meeting not found' });
    }
  } catch (error) {
    res.status(500).json({ error: 'Delete meeting failed' });
  }
};
