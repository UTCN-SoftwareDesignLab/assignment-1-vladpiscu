package repository.activity;

import model.Activity;

import java.sql.Date;
import java.util.List;

public interface ActivityRepository {
    List<Activity> getActivitiesForPeriod(Long userId, Date beginningOfPeriod, Date endingOfPeriod);
    boolean save(Long userId, Activity activity);
    void removeAll();
}
