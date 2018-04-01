package service.activity;

import model.Activity;
import repository.activity.ActivityRepository;

import java.sql.Date;
import java.util.List;

public class ActivityServiceMySQL implements ActivityService{
    private ActivityRepository activityRepository;

    public ActivityServiceMySQL(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public List<Activity> getActivityforPeriod(Long userId, Date beginningOfPeriod, Date endingOfPeriod) {
        return activityRepository.getActivitiesForPeriod(userId, beginningOfPeriod, endingOfPeriod);
    }

    @Override
    public boolean save(Activity activity) {
        return activityRepository.save(activity);
    }
}
