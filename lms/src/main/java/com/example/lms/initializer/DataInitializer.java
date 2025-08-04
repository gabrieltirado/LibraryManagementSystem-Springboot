package com.example.lms.initializer;

import com.example.lms.model.ERole;
import com.example.lms.model.Permission;
import com.example.lms.model.Role;
import com.example.lms.repository.PermissionRepository;
import com.example.lms.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public DataInitializer(RoleRepository roleRepository,
                           PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @PostConstruct
    public void seedRolesAndPermissions() {
        // Avoid duplicate seeding
        if (roleRepository.findByName(ERole.ADMIN).isPresent()) return;

        // Save permissions
        Permission manageUser = permissionRepository.save(new Permission(null, "MANAGE_USER"));
        Permission manageBook = permissionRepository.save(new Permission(null, "MANAGE_BOOK"));

        // Create ADMIN role with both permissions
        Role admin = new Role();
        admin.setName(ERole.ADMIN);
        admin.setPermissions(Set.of(manageUser, manageBook));
        roleRepository.save(admin);

        Role user = new Role();
        user.setName(ERole.USER);
        roleRepository.save(user);
    }

}