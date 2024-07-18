const express = require('express');
const cors = require('cors');
const app = express();
exports.app = app;

const userRoutes = require('./routes/userRoutes');
const meetingsRoutes = require('./routes/meetingsRoutes');
const meetingremindersRoutes = require('./routes/meetingremindersRoutes');
const meetingresultsRoutes = require('./routes/meetingresultsRoutes');
const participantsRoutes = require('./routes/participantsRoutes');
const summariesRoutes = require('./routes/summariesRoutes');
const summaryapprovalRoutes = require('./routes/summaryapprovalRoutes');


// Middleware to parse JSON bodies
app.use(express.json());
app.use(cors()); // Use CORS middleware

// Register your routes 
app.use('/api/user', userRoutes);
app.use('/api/meeting', meetingsRoutes);
app.use('/api/meetingreminders', meetingremindersRoutes);
app.use('/api/meetingresults', meetingresultsRoutes);
app.use('/api/participants', participantsRoutes);
app.use('/api/summaries', summariesRoutes);
app.use('/api/summaryapproval', summaryapprovalRoutes);

// Start the server
const PORT = process.env.PORT || 3500;
const HOST = '0.0.0.0'; // Bind to all network interfaces

app.listen(PORT, HOST, () => {
  console.log(`Server is running on http://${HOST}:${PORT}`);
});
