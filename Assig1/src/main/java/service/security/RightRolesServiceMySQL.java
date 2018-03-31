package service.security;

import model.Role;
import repository.security.RightsRolesRepository;

import java.util.ArrayList;
import java.util.List;

import static database.Constants.Roles.ROLES;

public class RightRolesServiceMySQL implements RightsRolesService {
    private final RightsRolesRepository rightsRolesRepository;

    public RightRolesServiceMySQL(RightsRolesRepository rightsRolesRepository){
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public List<Role> findAllRoles() {
        List<Role> roles = new ArrayList<>();
        for(String role : ROLES){
            roles.add(rightsRolesRepository.findRoleByTitle(role));
        }
        return roles;
    }
}
