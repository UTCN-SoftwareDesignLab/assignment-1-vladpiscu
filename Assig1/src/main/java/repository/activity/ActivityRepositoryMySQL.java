package repository.activity;

import model.Activity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.ACTIVITY;

public class ActivityRepositoryMySQL implements ActivityRepository {
    private final Connection connection;

    public ActivityRepositoryMySQL(Connection connection){
        this.connection = connection;
    }

    @Override
    public List<Activity> getActivitiesForPeriod(Long userId, Date startOfPeriod, Date endOfPeriod) {
        List<Activity> activities= new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String fetchUserSql = "Select * from `" + ACTIVITY + "` where `user_id`=\'" + userId + "\' AND `timestamp`>=\'" + startOfPeriod + "\' AND `timestamp`<=\'" + endOfPeriod + "\'";
            ResultSet activityResultSet = statement.executeQuery(fetchUserSql);
            while (activityResultSet.next()) {
                Activity activity = getActivityFromResultSet(activityResultSet);
                activities.add(activity);
            }
            return activities;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean save(Long userId, Activity activity) {
        try {
            PreparedStatement insertActivityStatement = connection
                    .prepareStatement("INSERT INTO activity values (null, ?, ?, ?)");
            insertActivityStatement.setLong(1, userId);
            insertActivityStatement.setString(2, activity.getOperation());
            insertActivityStatement.setDate(3, activity.getTimeStamp());
            insertActivityStatement.executeUpdate();

            ResultSet rs = insertActivityStatement.getGeneratedKeys();
            rs.next();
            long activityId = rs.getLong(1);
            activity.setId(activityId);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void removeAll() {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE from activity where id >= 0";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Activity getActivityFromResultSet(ResultSet rs) throws SQLException{
        Activity activity = new Activity(rs.getString("operation"), rs.getDate("timestamp"));
        activity.setId(rs.getLong("id"));
        return activity;
    }
}
