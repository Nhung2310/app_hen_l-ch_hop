const express = require('express');
const router = express.Router();
const summaryapprovalController = require('../controllers/summaryapprovalController');

router.get('/summaryapproval',summaryapprovalController.getAllSummaryApprovals );

module.exports = router;
