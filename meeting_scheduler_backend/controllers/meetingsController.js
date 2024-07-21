const db = require('../models');
const Meeting = db.Meeting;
const moment = require('moment'); // Make sure to install moment.js

exports.createMeeting = async (req, res) => {
  try {
    const { meeting_date, next_meeting_time, ...otherData } = req.body;

    // Try parsing the meeting date with multiple formats
    const dateFormats = ['DD/MM/YYYY', 'MM/DD/YYYY', 'YYYY-MM-DD', 'MMMM D, YYYY', 'DD-MM-YYYY'];
    let formattedDate = null;

    for (const format of dateFormats) {
      const date = moment(meeting_date, format, true); // true for strict parsing
      if (date.isValid()) {
        formattedDate = date.format('YYYY-MM-DD');
        break;
      }
    }

    if (!formattedDate) {
      return res.status(400).json({ error: 'Invalid meeting date format' });
    }

    // Try parsing the next meeting time
    const timeFormats = ['DD/MM/YYYY HH:mm', 'MM/DD/YYYY HH:mm', 'YYYY-MM-DD HH:mm', 'MMMM D, YYYY HH:mm', 'DD-MM-YYYY HH:mm'];
    let formattedNextMeetingTime = null;

    for (const format of timeFormats) {
      const time = moment(next_meeting_time, format, true); // true for strict parsing
      if (time.isValid()) {
        formattedNextMeetingTime = time.format('YYYY-MM-DD HH:mm:ss');
        break;
      }
    }

    if (!formattedNextMeetingTime) {
      return res.status(400).json({ error: 'Invalid next meeting time format' });
    }

    // Create the meeting with the formatted date and next meeting time
    const meeting = await Meeting.create({ meeting_date: formattedDate, next_meeting_time: formattedNextMeetingTime, ...otherData });

    if (meeting) {
      res.json(meeting);
    } else {
      return res.status(404).json({ error: 'Create meeting failed' });
    }

  } catch (error) {
    console.log("@@@@" + error);
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
