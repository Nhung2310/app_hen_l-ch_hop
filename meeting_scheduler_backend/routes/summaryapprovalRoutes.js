const express = require('express');
const router = express.Router();
const summaryapprovalController = require('../controllers/summaryapprovalController');

router.get('/summaryapproval',summaryapprovalController.getAllSummaryApprovals );
// Endpoint POST để tạo mới một summary approval
router.post('/summaryapproval', summaryapprovalController.createSummaryApproval);
module.exports = router;
