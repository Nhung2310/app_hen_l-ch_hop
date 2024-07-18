module.exports = (sequelize, DataTypes) => {
    const Participant = sequelize.define('Participant', {
        participant_id: {
            type: DataTypes.INTEGER,
            allowNull: false,
            primaryKey: true,
            autoIncrement: true
        },
        meeting_id: {
            type: DataTypes.INTEGER,
            allowNull: false
        },
        participant_name: {
            type: DataTypes.STRING(255),
            allowNull: false
        },
        email: {
            type: DataTypes.STRING(255),
            allowNull: false
        },
        role: {
            type: DataTypes.STRING(255),
            allowNull: true
        }
    }, {
        tableName: 'participants',
        timestamps: false,
        underscored: true
    });

    return Participant;
};
