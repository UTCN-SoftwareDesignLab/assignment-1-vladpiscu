package repository.user;

import database.DBConnectionFactory;
import model.Role;
import model.User;
import model.builder.UserBuilder;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.Parameterized;
import repository.account.AccountRepository;
import repository.account.AccountRepositoryMySQL;
import repository.client.ClientRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static database.Constants.Roles.ADMINISTRATOR;
import static database.Constants.Tables.USER;
import static org.junit.Assert.*;

public class UserRepositoryMySQLTest {
    private static UserRepository userRepository;
    private static RightsRolesRepository rightsRolesRepository;


    @BeforeClass
    public static void setUpClass() {
        Connection connection = new DBConnectionFactory().getConnectionWrapper(true).getConnection();
        rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
    }

    @Before
    public void cleanUp() {
        userRepository.removeAll();
    }

    @Test
    public void findAll(){
        List<Role> roles = new ArrayList<>();
        roles.add(rightsRolesRepository.findRoleByTitle(ADMINISTRATOR));
        User user = new UserBuilder()
                .setUsername("username")
                .setPassword("password")
                .setRoles(roles)
                .build();
        userRepository.save(user);
        user = new UserBuilder()
                .setUsername("username2")
                .setPassword("password")
                .setRoles(roles)
                .build();
        userRepository.save(user);
        user = new UserBuilder()
                .setUsername("username3")
                .setPassword("password")
                .setRoles(roles)
                .build();
        userRepository.save(user);
        assertEquals(3, userRepository.findAll().size());

    }

    @Test
    public void findByUsernameAndPassword() {
        List<Role> roles = new ArrayList<>();
        roles.add(rightsRolesRepository.findRoleByTitle(ADMINISTRATOR));
        User user = new UserBuilder()
                .setUsername("username")
                .setPassword("password")
                .setRoles(roles)
                .build();
        userRepository.save(user);
        User newUser;
        try{
            newUser = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword()).getResult();
            assertEquals(user, newUser);
        }
        catch (AuthenticationException e){
            e.printStackTrace();
        }

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
        assertTrue(userRepository.save(user));
    }

    @Test
    public void update() {
        List<Role> roles = new ArrayList<>();
        roles.add(rightsRolesRepository.findRoleByTitle(ADMINISTRATOR));
        User user = new UserBuilder()
                .setUsername("username")
                .setPassword("password")
                .setRoles(roles)
                .build();
        userRepository.save(user);
        user.setPassword("newPassword");
        assertTrue(userRepository.update(user));
    }

    @Test
    public void removeUser(){
        List<Role> roles = new ArrayList<>();
        roles.add(rightsRolesRepository.findRoleByTitle(ADMINISTRATOR));
        User user = new UserBuilder()
                .setUsername("username")
                .setPassword("password")
                .setRoles(roles)
                .build();
        userRepository.save(user);
        assertTrue(userRepository.removeUser(user.getId()));
    }

    @Test
    public void removeAll() {
        userRepository.removeAll();
        assertEquals(0, userRepository.findAll().size());
    }
}