package service.user;

import database.DBConnectionFactory;
import model.Role;
import model.User;
import model.builder.UserBuilder;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.security.PasswordEncoder;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Roles.ADMINISTRATOR;
import static org.junit.Assert.*;

public class UserServiceMySQLTest {
    private static UserRepository userRepository;
    private static RightsRolesRepository rightsRolesRepository;
    private static UserService userService;


    @BeforeClass
    public static void setUpClass() {
        Connection connection = new DBConnectionFactory().getConnectionWrapper(true).getConnection();
        rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        userService = new UserServiceMySQL(userRepository, new PasswordEncoder());
    }

    @Before
    public void cleanUp() {
        userRepository.removeAll();
    }

    @Test
    public void findAll() {
        List<Role> roles = new ArrayList<>();
        roles.add(rightsRolesRepository.findRoleByTitle(ADMINISTRATOR));
        User user = new UserBuilder()
                .setUsername("username1@test.com")
                .setPassword("passwordD.123")
                .setRoles(roles)
                .build();
        userRepository.save(user);
        user = new UserBuilder()
                .setUsername("username2@test.com")
                .setPassword("password5.D")
                .setRoles(roles)
                .build();
        userRepository.save(user);
        user = new UserBuilder()
                .setUsername("username3@test.com")
                .setPassword("password6.P")
                .setRoles(roles)
                .build();
        userRepository.save(user);
        assertEquals(3, userRepository.findAll().size());
    }

    @Test
    public void update() {
        List<Role> roles = new ArrayList<>();
        roles.add(rightsRolesRepository.findRoleByTitle(ADMINISTRATOR));
        User user = new UserBuilder()
                .setUsername("username@test.com")
                .setPassword("passw0rD.")
                .setRoles(roles)
                .build();
        userRepository.save(user);
        user.setPassword("newPassword.0");
        assertTrue(userService.update(user.getId(), user.getUsername(), user.getPassword(), user.getRoles(), true).getResult());
        user.setPassword("newPassword");
        assertTrue(userService.update(user.getId(), user.getUsername(), user.getPassword(), user.getRoles(), true).hasErrors());

    }

    @Test
    public void save() {
        List<Role> roles = new ArrayList<>();
        roles.add(rightsRolesRepository.findRoleByTitle(ADMINISTRATOR));
        User user = new UserBuilder()
                .setUsername("username")
                .setPassword("password")
                .setRoles(roles)
                .build();
        assertTrue(userService.save(user.getUsername(), user.getPassword(), user.getRoles()).hasErrors());
        User user1 = new UserBuilder()
                .setUsername("username@test.com")
                .setPassword("passworD.0")
                .setRoles(roles)
                .build();
        assertTrue(userService.save(user1.getUsername(), user1.getPassword(), user1.getRoles()).getResult());
    }

    @Test
    public void removeUser() {
        List<Role> roles = new ArrayList<>();
        roles.add(rightsRolesRepository.findRoleByTitle(ADMINISTRATOR));
        User user = new UserBuilder()
                .setUsername("username")
                .setPassword("password")
                .setRoles(roles)
                .build();
        userRepository.save(user);
        assertTrue(userService.removeUser(user.getId()));
    }
}