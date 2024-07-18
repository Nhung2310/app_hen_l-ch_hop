module.exports = (sequelize, DataTypes) => {
    const MeetingReminder = sequelize.define('MeetingReminder', {
      reminder_id: {
        type: DataTypes.INTEGER,
        allowNull: false,
        primaryKey: true,
        autoIncrement: true
      },
      meeting_id: {
        type: DataTypes.INTEGER,
        allowNull: false
      },
      reminder_time: {
        type: DataTypes.DATE,
        allowNull: false
      },
      reminder_description: {
        type: DataTypes.TEXT,
        allowNull: true
      },
      user_id: {
        type: DataTypes.INTEGER,
        allowNull: false
      },
      status: {
        type: DataTypes.STRING(50),
        allowNull: true
      },
      created_at: {
        type: DataTypes.DATE,
        allowNull: true,
        defaultValue: DataTypes.NOW
      },
      updated_at: {
        type: DataTypes.DATE,
        allowNull: true,
        defaultValue: DataTypes.NOW
      }
    }, {
      tableName: 'meetingreminders',
      timestamps: false,
      underscored: true
    });
  
    return MeetingReminder;
  };
  