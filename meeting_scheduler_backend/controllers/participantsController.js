const db = require('../models');
const Participant = db.Participant;

exports.createParticipant = async (req, res) => {
  try {
    const participant = await Participant.create(req.body);

    if (participant) {
      res.json(participant);
    } else {
      return res.status(404).json({ error: 'Create participant failed' });
    }

  } catch (error) {
    res.status(500).json({ error: 'Create participant failed' });
  }
};

exports.getAllParticipants = async (req, res) => {
  try {
    const participants = await Participant.findAll();
    res.json(participants);
  } catch (error) {
    res.status(500).json({ error: 'Get all participants failed' });
  }
};

exports.getParticipantById = async (req, res) => {
  try {
    const participant = await Participant.findByPk(req.params.id);
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
    const participant = await Participant.findByPk(req.params.id);
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
    const participant = await Participant.findByPk(req.params.id);
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
