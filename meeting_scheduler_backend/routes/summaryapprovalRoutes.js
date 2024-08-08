const express = require('express');
const router = express.Router();
const summaryapprovalController = require('../controllers/summaryapprovalController');

router.get('/summaryapproval',summaryapprovalController.getAllSummaryApprovals );
// Endpoint POST để tạo mới một summary approval
router.post('/summaryapproval', summaryapprovalController.createSummaryApproval);

/// Endpoint GET để lấy tất cả summary approvals theo summary_id
router.get('/summaryapproval/by-summary-id/:summary_id', summaryapprovalController.getSummaryApprovalsBySummaryId);

module.exports = router;
