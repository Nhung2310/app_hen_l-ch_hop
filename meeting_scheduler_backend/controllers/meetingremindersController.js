const db = require('../models');
const MeetingReminder = db.MeetingReminder;

exports.createMeetingReminder = async (req, res) => {
  try {
    const meetingReminder = await MeetingReminder.create(req.body);

    if (meetingReminder) {
      res.json(meetingReminder);
    } else {
      return res.status(404).json({ error: 'Create meeting reminder failed' });
    }

  } catch (error) {
    res.status(500).json({ error: 'Create meeting reminder failed' });
  }
};

exports.getAllMeetingReminders = async (req, res) => {
  try {
    const meetingReminders = await MeetingReminder.findAll();
    res.json(meetingReminders);
  } catch (error) {
    res.status(500).json({ error: 'Get all meeting reminders failed' });
  }
};

exports.getMeetingReminderById = async (req, res) => {
  try {
    const meetingReminder = await MeetingReminder.findByPk(req.params.id);
    if (meetingReminder) {
      res.json(meetingReminder);
    } else {
      res.status(404).json({ error: 'Meeting reminder not found' });
    }
  } catch (error) {
    res.status(500).json({ error: 'Get meeting reminder by ID failed' });
  }
};

exports.updateMeetingReminder = async (req, res) => {
  try {
    const meetingReminder = await MeetingReminder.findByPk(req.params.id);
    if (meetingReminder) {
      await meetingReminder.update(req.body);
      res.json(meetingReminder);
    } else {
      res.status(404).json({ error: 'Meeting reminder not found' });
    }
  } catch (error) {
    res.status(500).json({ error: 'Update meeting reminder failed' });
  }
};

exports.deleteMeetingReminder = async (req, res) => {
  try {
    const meetingReminder = await MeetingReminder.findByPk(req.params.id);
    if (meetingReminder) {
      await meetingReminder.destroy();
      res.json({ message: 'Meeting reminder deleted' });
    } else {
      res.status(404).json({ error: 'Meeting reminder not found' });
    }
  } catch (error) {
    res.status(500).json({ error: 'Delete meeting reminder failed' });
  }
};
