const db = require('../models');
const MeetingResult = db.MeetingResult;

// Create a meeting result
exports.createMeetingResult = async (req, res) => {
  try {
    const meetingResult = await MeetingResult.create(req.body);

    if (meetingResult) {
      res.json(meetingResult);
    } else {
      return res.status(404).json({ error: 'Create meeting result failed' });
    }

  } catch (error) {
    console.error(error);
    res.status(500).json({ error: 'Create meeting result failed' });
  }
};

// Get all meeting results
exports.getAllMeetingResults = async (req, res) => {
  try {
    const meetingResults = await MeetingResult.findAll();
    res.json(meetingResults);
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: 'Get all meeting results failed' });
  }
};

// Get meeting result by ID
exports.getMeetingResultById = async (req, res) => {
  try {
    const meetingResult = await MeetingResult.findByPk(req.params.id);
    if (meetingResult) {
      res.json(meetingResult);
    } else {
      res.status(404).json({ error: 'Meeting result not found' });
    }
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: 'Get meeting result by ID failed' });
  }
};

// Update meeting result
exports.updateMeetingResult = async (req, res) => {
  try {
    const meetingResult = await MeetingResult.findByPk(req.params.id);
    if (meetingResult) {
      await meetingResult.update(req.body);
      res.json(meetingResult);
    } else {
      res.status(404).json({ error: 'Meeting result not found' });
    }
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: 'Update meeting result failed' });
  }
};

// Delete meeting result
exports.deleteMeetingResult = async (req, res) => {
  try {
    const meetingResult = await MeetingResult.findByPk(req.params.id);
    if (meetingResult) {
      await meetingResult.destroy();
      res.json({ message: 'Meeting result deleted' });
    } else {
      res.status(404).json({ error: 'Meeting result not found' });
    }
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: 'Delete meeting result failed' });
  }
};
