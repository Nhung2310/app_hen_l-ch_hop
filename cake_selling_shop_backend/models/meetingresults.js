module.exports = (sequelize, DataTypes) => {
    const MeetingResult = sequelize.define('MeetingResult', {
        result_id: {
            type: DataTypes.INTEGER,
            allowNull: false,
            primaryKey: true,
            autoIncrement: true
        },
        meeting_id: {
            type: DataTypes.INTEGER,
            allowNull: false
        },
        result: {
            type: DataTypes.TEXT,
            allowNull: true
        },
        new_requirements: {
            type: DataTypes.TEXT,
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
        tableName: 'meetingresults',
        timestamps: false, // Set to true if your table has timestamps
        underscored: true
    });

    return MeetingResult;
};
