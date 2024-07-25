const { Sequelize, DataTypes } = require('sequelize');
const config = require('../config/config.json');
const env = process.env.NODE_ENV || 'development';
const dbConfig = config[env];

const sequelize = new Sequelize(dbConfig.database, dbConfig.username, dbConfig.password, {
  host: dbConfig.host,
  dialect: dbConfig.dialect
});

const db = {};
db.Sequelize = Sequelize;
db.sequelize = sequelize;

// Import models
db.User = require('./user')(sequelize, DataTypes);
db.Meeting = require('./meetings')(sequelize, DataTypes);
db.MeetingReminder = require('./meetingreminders')(sequelize, DataTypes);
db.MeetingResult = require('./meetingresults')(sequelize, DataTypes);
db.MeetingParticipant=require('./meetingparticipants')(sequelize, DataTypes);
db.Summary =require('./summaries')(sequelize, DataTypes);
db.SummaryApproval=require('./summaryapproval')(sequelize, DataTypes);

// Setup associations
Object.keys(db).forEach(modelName => {
  if (db[modelName].associate) {
      db[modelName].associate(db);
  }
});

module.exports = db;
