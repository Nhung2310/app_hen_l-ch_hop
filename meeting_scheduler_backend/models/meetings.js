module.exports = (sequelize, DataTypes) => {
    const Meeting = sequelize.define('Meeting', {
      meeting_id: {
        type: DataTypes.INTEGER,
        allowNull: false,
        primaryKey: true,
        autoIncrement: true
      },
      title: {
        type: DataTypes.STRING(255),
        allowNull: false
      },
      meeting_date: {
        type: DataTypes.DATE,
        allowNull: false
      },
      start_time: {
        type: DataTypes.TIME,
        allowNull: true
      },
      end_time: {
        type: DataTypes.TIME,
        allowNull: true
      },
      location: {
        type: DataTypes.STRING(255),
        allowNull: false
      },
      agenda: {
        type: DataTypes.TEXT,
        allowNull: true
      },
      documents: {
        type: DataTypes.TEXT,
        allowNull: true
      },
      result: {
        type: DataTypes.TEXT,
        allowNull: true
      },
      next_meeting_time: {
        type: DataTypes.DATE,
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
      tableName: 'meetings',
      timestamps: false,
      underscored: true
    });
  
    return Meeting;
  };
