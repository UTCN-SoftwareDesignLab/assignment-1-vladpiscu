package controller;

import database.DBConnectionFactory;
import repository.account.AccountRepository;
import repository.account.AccountRepositoryMySQL;
import repository.client.ClientRepository;
import repository.client.ClientRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.client.ClientService;
import service.client.ClientServiceMySQL;
import service.security.PasswordEncoder;
import service.security.RightRolesServiceMySQL;
import service.security.RightsRolesService;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceMySQL;
import service.user.UserService;
import service.user.UserServiceMySQL;

import java.sql.Connection;

public class ComponentFactory {
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final RightsRolesService rightsRolesService;

    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;
    private final ClientService clientService;

    private static ComponentFactory instance;

    public static ComponentFactory instance() {
        if (instance == null) {
            instance = new ComponentFactory();
        }
        return instance;
    }

    private ComponentFactory() {
        Connection connection = new DBConnectionFactory().getConnectionWrapper(false).getConnection();
        PasswordEncoder encoder = new PasswordEncoder();
        AccountRepository accountRepository = new AccountRepositoryMySQL(connection);
        ClientRepository clientRepository = new ClientRepositoryMySQL(connection, accountRepository);
        this.clientService = new ClientServiceMySQL(clientRepository);
        this.rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        this.userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        this.authenticationService = new AuthenticationServiceMySQL(this.userRepository, this.rightsRolesRepository, encoder);
        this.userService = new UserServiceMySQL(userRepository, encoder);
        this.rightsRolesService = new RightRolesServiceMySQL(rightsRolesRepository);
    }

    public ClientService getClientService() {
        return clientService;
    }

    public RightsRolesService getRightsRolesService() {
        return rightsRolesService;
    }

    public UserService getUserService() {
        return userService;
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public RightsRolesRepository getRightsRolesRepository() {
        return rightsRolesRepository;
    }
}
