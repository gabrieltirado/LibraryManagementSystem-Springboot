package com.example.lms.service;

import com.example.lms.model.ERole;
import com.example.lms.model.Role;
import com.example.lms.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    void testAddRole() {
        Role role = new Role();
        ERole roleName = ERole.ADMIN;
        role.setName(roleName);
        when(roleRepository.save(role)).thenReturn(role);

        Role savedRole = roleService.addRole(role);

        assertEquals("ADMIN", savedRole.getName().toString());
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void testUpdateRole() {
        Role existingRole = new Role();
        ERole roleAdmin = ERole.ADMIN;
        ERole roleUser = ERole.USER;
        existingRole.setId(1L);
        existingRole.setName(roleUser);

        Role updatedDetails = new Role();
        updatedDetails.setName(roleAdmin);

        when(roleRepository.findById(1L)).thenReturn(Optional.of(existingRole));
        when(roleRepository.save(any(Role.class))).thenReturn(existingRole);

        Role updatedRole = roleService.updateRole(1L, updatedDetails);

        assertEquals("ADMIN", updatedRole.getName().toString());
        verify(roleRepository).findById(1L);
        verify(roleRepository).save(existingRole);
    }

    @Test
    void testDeleteRole() {
        doNothing().when(roleRepository).deleteById(1L);

        roleService.deleteRole(1L);

        verify(roleRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetAllRoles() {
        List<Role> roles = Arrays.asList(new Role(), new Role());
        when(roleRepository.findAll()).thenReturn(roles);

        List<Role> result = roleService.getAllRoles();

        assertEquals(2, result.size());
        verify(roleRepository).findAll();
    }

    @Test
    void testGetRoleById() {
        Role role = new Role();
        role.setId(1L);
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        Optional<Role> result = roleService.getRoleById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(roleRepository).findById(1L);
    }
}