package service.user;

import model.Role;
import model.User;
import model.validation.Notification;

import java.util.List;

public interface UserService {
    List<User> findAll();
    Notification<Boolean> update(Long id, String username, String password, List<Role> roles, boolean updatePassword);
    Notification<Boolean> save(String username, String password, List<Role> roles);
    boolean removeUser(Long userId);
}
