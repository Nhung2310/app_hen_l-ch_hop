const cron = require('node-cron');
const db = require('../models');
const Meeting = db.Meeting;
const moment = require('moment-timezone'); // Use moment-timezone if you need to handle timezones
const axios = require('axios');
const multer = require('multer');
const path = require('path');
const fs = require('fs')

// Configure multer for file uploads
const storage = multer.diskStorage({
  destination: (req, file, cb) => {
    cb(null, 'uploads/');
  },
  filename: (req, file, cb) => {
    cb(null, `${Date.now()}-${file.originalname}`);
  }
});

const upload = multer({ storage: storage });

// Schedule a job to run every minute
cron.schedule('* * * * *', async () => {
  try {
    const now = moment();
    const currentDate = now.format('YYYY-MM-DD');
    const currentTime = now.format('HH:mm:ss');

    let meetingDate = moment.tz(currentDate, "YYYY-MM-DD HH:mm:ss", "UTC")
      .add(7, 'hours')
      .format('YYYY-MM-DD HH:mm:ss');
    let startTime = moment.tz(currentTime, "HH:mm:ss", "UTC")
      .add(30, 'minutes')
      .format('HH:mm:ss');

    // Find meetings that are scheduled to start at the current date and time
    const meetings = await Meeting.findAll({
      where: {
        meeting_date: meetingDate,
        start_time: startTime
      }
    });

    // Send email for each meeting found
    for (const meeting of meetings) {
      const allMembers = await db.MeetingParticipant.findAll({
        where: {
          meeting_id: meeting.meeting_id
        }
      });

      for (const member of allMembers) {
        await sendEmail(1, member, meeting);
      }

      console.log(`Email sent for meeting ID: ${meeting.meeting_id}`);
    }
  } catch (error) {
    console.error('Error checking meetings:', error);
  }
});

const sendEmail = async (option, member, meeting) => {
  await axios.post('http://localhost:3500/api/email/send-email', { 
    option: option,
    email: member.email, 
    title: meeting.title,
    agenda: meeting.agenda,
    meeting_date: meeting.meeting_date,
    start_time: meeting.start_time,
    end_time: meeting.end_time,
    location: meeting.location
  });
}

exports.createMeeting = async (req, res) => {
  try {
    const { meeting_date, next_meeting_time, participants, ...otherData } = req.body;

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
      // Fetch all participants associated with the meeting
      const allMembers = await db.MeetingParticipant.findAll({
        where: {
          meeting_id: meeting.meeting_id
        }
      });

      // Send email to each participant
      for (const member of allMembers) {
        await sendEmail(0, member, meeting);
      }

      res.json(meeting);
    } else {
      return res.status(404).json({ error: 'Create meeting failed' });
    }

  } catch (error) {
    res.status(500).json({ error: 'Create meeting failed' });
  }
};

// Add a new route to handle file uploads and meeting creation
exports.createMeetingWithFiles = [
  upload.array('files', 10), // Handle up to 10 files
  async (req, res) => {
    try {
      const { meeting_date, next_meeting_time, participants, ...otherData } = req.body;

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
        // Save file information to the meeting (if needed)
        const files = req.files;
        if (files && files.length > 0) {
          const fileInfos = files.map(file => ({
            filename: file.filename,
            path: file.path,
            originalname: file.originalname,
            mimetype: file.mimetype,
            size: file.size
          }));
          // You can save fileInfos to the database if needed
          meeting.documents = files[0].filename;
          await meeting.save();
        }

        // Fetch all participants associated with the meeting
        const allMembers = await db.MeetingParticipant.findAll({
          where: {
            meeting_id: meeting.meeting_id
          }
        });

        // Send email to each participant
        for (const member of allMembers) {
          await sendEmail(0, member, meeting);
        }

        res.json(meeting);
      } else {
        console.log("@@@");
        return res.status(404).json({ error: 'Create meeting failed' });
      }

    } catch (error) {
      console.error("An error occurred:", error);
      res.status(500).json({ error: 'Create meeting failed' });
    }
  }
];

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

exports.downloadFile = (req, res) => {
  const filename = req.params.filename;
  const filePath = path.join(__dirname, '../uploads', filename);

  // Check if the file exists
  fs.access(filePath, fs.constants.F_OK, (err) => {
    if (err) {
      return res.status(404).json({ error: 'File not found' });
    }

    // Send the file to the client
    res.download(filePath, filename, (err) => {
      if (err) {
        return res.status(500).json({ error: 'Error downloading file' });
      }
    });
  });
};
