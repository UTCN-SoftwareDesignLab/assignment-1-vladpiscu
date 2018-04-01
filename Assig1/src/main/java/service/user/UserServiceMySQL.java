package service.user;

import model.Role;
import model.User;
import model.builder.UserBuilder;
import model.validation.Notification;
import model.validation.UserValidator;
import repository.user.UserRepository;
import service.security.PasswordEncoder;
import java.util.List;

public class UserServiceMySQL implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserServiceMySQL(UserRepository userRepository, PasswordEncoder encoder){
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Notification<Boolean> update(Long id, String username, String password, List<Role> roles, boolean updatePassword) {
        User user = new UserBuilder()
                .setId(id)
                .setUsername(username)
                .setPassword(password)
                .setRoles(roles)
                .build();
        Notification<Boolean> userUpdateNotification = new Notification<>();
        if(updatePassword) {
            UserValidator userValidator = new UserValidator(user);
            boolean userValid = userValidator.validate();
            if (!userValid) {
                userValidator.getErrors().forEach(userUpdateNotification::addError);
                userUpdateNotification.setResult(Boolean.FALSE);
                return userUpdateNotification;
            } else {
                user.setPassword(encoder.encodeString(password));
                userUpdateNotification.setResult(userRepository.update(user));
                return userUpdateNotification;
            }

        }
        else {
            userUpdateNotification.setResult(userRepository.update(user));
            return userUpdateNotification;
        }
    }

    @Override
    public Notification<Boolean> save(String username, String password, List<Role> roles) {
        User user = new UserBuilder()
                .setUsername(username)
                .setPassword(password)
                .setRoles(roles)
                .build();

        UserValidator userValidator = new UserValidator(user);
        boolean userValid = userValidator.validate();
        Notification<Boolean> userRegisterNotification = new Notification<>();

        if (!userValid) {
            userValidator.getErrors().forEach(userRegisterNotification::addError);
            userRegisterNotification.setResult(Boolean.FALSE);
            return userRegisterNotification;
        } else {
            user.setPassword(encoder.encodeString(password));
            userRegisterNotification.setResult(userRepository.save(user));
            return userRegisterNotification;
        }
    }

    @Override
    public boolean removeUser(Long userId) {
        return userRepository.removeUser(userId);
    }

}
