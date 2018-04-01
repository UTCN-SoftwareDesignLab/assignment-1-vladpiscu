package service.activity;

import database.DBConnectionFactory;
import model.Activity;
import model.Role;
import model.User;
import model.builder.UserBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.activity.ActivityRepository;
import repository.activity.ActivityRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Roles.EMPLOYEE;
import static org.junit.Assert.*;

public class ActivityServiceMySQLTest {

    private static ActivityRepository activityRepository;
    private static Long userId;
    private static List<Role> roles;
    private static UserRepository userRepository;
    private static ActivityService activityService;

    @BeforeClass
    public static void setUpClass() {
        Connection connection = new DBConnectionFactory().getConnectionWrapper(true).getConnection();
        activityRepository = new ActivityRepositoryMySQL(connection);
        roles = new ArrayList<>();
        RightsRolesRepository rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        roles.add(rightsRolesRepository.findRoleByTitle(EMPLOYEE));
        userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        activityService = new ActivityServiceMySQL(activityRepository);
    }

    @Before
    public void setUp() {
        activityRepository.removeAll();
        User user = new UserBuilder()
                .setUsername("user")
                .setPassword("password")
                .setRoles(roles)
                .build();
        userRepository.save(user);
        userId = user.getId();
    }

    @After
    public void removeUser() {
        userRepository.removeAll();
    }

    @Test
    public void getActivityforPeriod() {
        Date date1 = new Date(2018, 10, 1);
        Date date2 = new Date(2018, 10, 15);
        Date date3 = new Date(2018, 10, 21);
        Date date4 = new Date(2018, 11, 15);
        Date date5 = new Date(2018, 12, 15);
        Activity activity1 = new Activity("operation", date1);
        Activity activity2 = new Activity("operation", date2);
        Activity activity3 = new Activity("operation", date3);
        Activity activity4 = new Activity("operation", date4);
        Activity activity5 = new Activity("operation", date5);
        activity1.setUserId(userId);
        activity2.setUserId(userId);
        activity3.setUserId(userId);
        activity4.setUserId(userId);
        activity5.setUserId(userId);
        activityRepository.save(activity1);
        activityRepository.save(activity2);
        activityRepository.save(activity3);
        activityRepository.save(activity4);
        activityRepository.save(activity5);
        Date endDate = new Date(2018, 10, 30);
        assertEquals(3, activityService.getActivityforPeriod(userId, date1, endDate).size());
    }

    @Test
    public void save() {
        Activity activity = new Activity("operation", new Date(2018, 10, 1));
        activity.setUserId(userId);
        assertTrue(activityService.save(activity));
    }
}