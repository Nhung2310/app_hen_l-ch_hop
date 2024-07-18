module.exports = (sequelize, DataTypes) => {
    const SummaryApproval = sequelize.define('SummaryApproval', {
      approval_id: {
        type: DataTypes.INTEGER,
        allowNull: false,
        primaryKey: true,
        autoIncrement: true
      },
      summary_id: {
        type: DataTypes.INTEGER,
        allowNull: false
      },
      user_id: {
        type: DataTypes.INTEGER,
        allowNull: false
      },
      approval_status: {
        type: DataTypes.STRING(50),
        allowNull: false
      },
      review_suggestion: {
        type: DataTypes.STRING(255),
        allowNull: true
      },
      created_at: {
        type: DataTypes.DATE,
        allowNull: true
      },
      updated_at: {
        type: DataTypes.DATE,
        allowNull: true
      }
    }, {
      tableName: 'summaryapproval',
      timestamps: false, // Nếu bạn đã có cột `created_at` và `updated_at` trong bảng SQL
      underscored: true
    });
  
    return SummaryApproval;
  };
  