package service.user;

import model.Role;
import model.User;
import model.builder.UserBuilder;
import model.validation.Notification;
import model.validation.UserValidator;
import repository.security.RightsRolesRepository;
import repository.user.AuthenticationException;
import repository.user.UserRepository;
import service.security.PasswordEncoder;

import java.util.Collections;

import static database.Constants.Roles.ADMINISTRATOR;

public class AuthenticationServiceMySQL implements AuthenticationService{
    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;
    private final PasswordEncoder encoder;

    public AuthenticationServiceMySQL(UserRepository userRepository, RightsRolesRepository rightsRolesRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.rightsRolesRepository = rightsRolesRepository;
        this.encoder = encoder;
    }

    @Override
    public Notification<User> login(String username, String password) throws AuthenticationException {
        return userRepository.findByUsernameAndPassword(username, encoder.encodeString(password));
    }

    @Override
    public boolean logout(User user) {
        return false;
    }
}
