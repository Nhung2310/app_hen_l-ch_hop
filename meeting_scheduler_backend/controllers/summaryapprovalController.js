const db = require('../models');
const SummaryApproval = db.SummaryApproval;

exports.createSummaryApproval = async (req, res) => {
  try {
    const summaryApproval = await SummaryApproval.create(req.body);

    if (summaryApproval) {
      res.json(summaryApproval);
    } else {
      return res.status(404).json({ error: 'Create summary approval failed' });
    }

  } catch (error) {
    console.error('Error creating summary approval:', error); 
    res.status(500).json({ error: 'Create summary approval failed' });
  }
};

exports.getAllSummaryApprovals = async (req, res) => {
  try {
    const summaryApprovals = await SummaryApproval.findAll();
    res.json(summaryApprovals);
  } catch (error) {
    res.status(500).json({ error: 'Get all summary approvals failed' });
  }
};

exports.getSummaryApprovalById = async (req, res) => {
  try {
    const summaryApproval = await SummaryApproval.findByPk(req.params.id);
    if (summaryApproval) {
      res.json(summaryApproval);
    } else {
      res.status(404).json({ error: 'Summary approval not found' });
    }
  } catch (error) {
    res.status(500).json({ error: 'Get summary approval by ID failed' });
  }
};

exports.updateSummaryApproval = async (req, res) => {
  try {
    const summaryApproval = await SummaryApproval.findByPk(req.params.id);
    if (summaryApproval) {
      await summaryApproval.update(req.body);
      res.json(summaryApproval);
    } else {
      res.status(404).json({ error: 'Summary approval not found' });
    }
  } catch (error) {
    res.status(500).json({ error: 'Update summary approval failed' });
  }
};

exports.deleteSummaryApproval = async (req, res) => {
  try {
    const summaryApproval = await SummaryApproval.findByPk(req.params.id);
    if (summaryApproval) {
      await summaryApproval.destroy();
      res.json({ message: 'Summary approval deleted' });
    } else {
      res.status(404).json({ error: 'Summary approval not found' });
    }
  } catch (error) {
    res.status(500).json({ error: 'Delete summary approval failed' });
  }
};
