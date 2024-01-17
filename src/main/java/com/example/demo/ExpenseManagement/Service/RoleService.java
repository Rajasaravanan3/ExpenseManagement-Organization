package com.example.demo.ExpenseManagement.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.ExpenseManagement.Entity.Role;
import com.example.demo.ExpenseManagement.ExceptionController.ApplicationException;
import com.example.demo.ExpenseManagement.ExceptionController.ValidationException;
import com.example.demo.ExpenseManagement.Repository.RoleRepository;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;
    
    public Role getRoleById(Long roleId) {
        
        Role role = null;
        try {
            role = roleRepository.findRoleById(roleId);
            if(role == null)
                throw new ValidationException("No record found for the roleId " + roleId, HttpStatus.NOT_FOUND);
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while retrieving role by Id "+ roleId);
        }
        return role;
    }

    public void addRole(Role role) {

        try {
            if(role == null ||
            (role.getRoleName() instanceof String && (role.getRoleName().isEmpty() || role.getRoleName().length() > 40)) ||
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

            if(updatedRole.getRoleName() instanceof String || ! (updatedRole.getRoleName().isEmpty())) {

                if(updatedRole.getRoleName().length() > 40)
                    throw new ValidationException("Role name must not exceed 40 characters", HttpStatus.BAD_REQUEST);
                existingRole.setRoleName(updatedRole.getRoleName());
            }

            if(updatedRole.getRoleDescription() instanceof String || ! (updatedRole.getRoleDescription().isEmpty())) {

                if(updatedRole.getRoleDescription().length() > 300)
                    throw new ValidationException("Role description must not exceed 300 characters", HttpStatus.BAD_REQUEST);
                existingRole.setRoleDescription(updatedRole.getRoleDescription());
            }

            if(updatedRole.getIsActive() instanceof Boolean){

                existingRole.setIsActive(updatedRole.getIsActive());
            }
            roleRepository.save(existingRole);
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while updating the role.");
        }
    }
}
