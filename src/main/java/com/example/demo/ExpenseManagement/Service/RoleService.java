package com.example.demo.ExpenseManagement.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.ExpenseManagement.DTO.UserDTO;
import com.example.demo.ExpenseManagement.Entity.Role;
import com.example.demo.ExpenseManagement.Entity.User;
import com.example.demo.ExpenseManagement.ExceptionController.ApplicationException;
import com.example.demo.ExpenseManagement.ExceptionController.ValidationException;
import com.example.demo.ExpenseManagement.Repository.RoleRepository;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;
    
    public Role getRoleById(Long roleId) {
        
        Role role = null;
        try {
            role = roleRepository.findRoleById(roleId);
            if(role == null)
                throw new ValidationException("No record found for the roleId " + roleId, HttpStatus.NOT_FOUND);
            if(!role.getIsActive()) {
                throw new ValidationException("This role is not active right now", HttpStatus.FORBIDDEN);
            }
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while retrieving role by Id "+ roleId);
        }
        return role;
    }

    public List<UserDTO> getUsersByRoleName(Long organizationId, String roleName) {
        List<User> users = new ArrayList<>();
        List<UserDTO> userDTOList = new ArrayList<>();
        try {
            users = roleRepository.findUsersByRoleName(organizationId, roleName);
            
            if(users == null) {
                throw new ValidationException("No user found in the role " + roleName +" working for the organization id " + organizationId, null);
            }

            for (User user : users) {
                userDTOList.add(userService.mapUserToUserDTO(user));
            }
            return userDTOList;
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occured while retrieving users working as " + roleName + " in the organization id " + organizationId);
        }
    }

    public void addRole(Role role) {

        try {
            if(role == null ||
            (role.getRoleName() instanceof String && (role.getRoleName().isEmpty() || role.getRoleName().length() > 50)) ||
            (role.getRoleDescription() instanceof String && (role.getRoleDescription().length() > 300))) {

                throw new ValidationException("Non null field value must not be null or empty and characters must not exceed limit.", HttpStatus.BAD_REQUEST);
            }
            roleRepository.saveAndFlush(role);
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while saving the role.");
        }
    }

    public void updateRole(Role updatedRole) {
        
        Role existingRole = null;
        try {
            existingRole = roleRepository.findRoleById(updatedRole.getRoleId());
            
            if(existingRole == null) {
                throw new ValidationException("No record found to update for the role id " + updatedRole.getRoleId(), HttpStatus.NOT_FOUND);
            }

            if(updatedRole.getRoleName() instanceof String && ! (updatedRole.getRoleName().isEmpty())) {

                if(updatedRole.getRoleName().length() > 50)
                    throw new ValidationException("Role name must not exceed 50 characters", HttpStatus.BAD_REQUEST);
                existingRole.setRoleName(updatedRole.getRoleName());
            }

            if(updatedRole.getRoleDescription() instanceof String && ! (updatedRole.getRoleDescription().isEmpty())) {

                if(updatedRole.getRoleDescription().length() > 300)
                    throw new ValidationException("Role description must not exceed 300 characters", HttpStatus.BAD_REQUEST);
                existingRole.setRoleDescription(updatedRole.getRoleDescription());
            }

            if(updatedRole.getIsActive() instanceof Boolean){

                existingRole.setIsActive(updatedRole.getIsActive());
            }
            roleRepository.saveAndFlush(existingRole);
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while updating the role.");
        }
    }
}
