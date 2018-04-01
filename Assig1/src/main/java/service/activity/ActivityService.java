package service.activity;

import model.Activity;

import java.sql.Date;
import java.util.List;

public interface ActivityService {
    List<Activity> getActivityforPeriod(Long userId, Date beginningOfPeriod, Date endingOfPeriod);
    boolean save(Activity activity);
}
