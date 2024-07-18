const db = require('../models');
const Summary = db.Summary;

exports.createSummary = async (req, res) => {
  try {
    const summary = await Summary.create(req.body);

    if (summary) {
      res.json(summary);
    } else {
      return res.status(404).json({ error: 'Create summary failed' });
    }

  } catch (error) {
    res.status(500).json({ error: 'Create summary failed' });
  }
};

exports.getAllSummaries = async (req, res) => {
  try {
    const summaries = await Summary.findAll();
    res.json(summaries);
  } catch (error) {
    res.status(500).json({ error: 'Get all summaries failed' });
  }
};

exports.getSummaryById = async (req, res) => {
  try {
    const summary = await Summary.findByPk(req.params.id);
    if (summary) {
      res.json(summary);
    } else {
      res.status(404).json({ error: 'Summary not found' });
    }
  } catch (error) {
    res.status(500).json({ error: 'Get summary by ID failed' });
  }
};

exports.updateSummary = async (req, res) => {
  try {
    const summary = await Summary.findByPk(req.params.id);
    if (summary) {
      await summary.update(req.body);
      res.json(summary);
    } else {
      res.status(404).json({ error: 'Summary not found' });
    }
  } catch (error) {
    res.status(500).json({ error: 'Update summary failed' });
  }
};

exports.deleteSummary = async (req, res) => {
  try {
    const summary = await Summary.findByPk(req.params.id);
    if (summary) {
      await summary.destroy();
      res.json({ message: 'Summary deleted' });
    } else {
      res.status(404).json({ error: 'Summary not found' });
    }
  } catch (error) {
    res.status(500).json({ error: 'Delete summary failed' });
  }
};
