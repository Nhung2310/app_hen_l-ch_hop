module.exports = (sequelize, DataTypes) => {
    const Summary = sequelize.define('Summary', {
      summary_id: {
        type: DataTypes.INTEGER,
        allowNull: false,
        primaryKey: true,
        autoIncrement: true
      },
      meeting_id: {
        type: DataTypes.INTEGER,
        allowNull: false
      },
      conclusion: {
        type: DataTypes.TEXT,
        allowNull: false
      },
      coordinator_id: {
        type: DataTypes.INTEGER,
        allowNull: false
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
      tableName: 'summaries',
      timestamps: false,
      underscored: true
    });
  
    return Summary;
  };
  