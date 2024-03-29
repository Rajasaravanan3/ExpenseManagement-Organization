package com.example.demo.ExpenseManagement.Service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.ExpenseManagement.DTO.UserDTO;
import com.example.demo.ExpenseManagement.Entity.Role;
import com.example.demo.ExpenseManagement.Entity.User;
import com.example.demo.ExpenseManagement.ExceptionController.ApplicationException;
import com.example.demo.ExpenseManagement.ExceptionController.ValidationException;
import com.example.demo.ExpenseManagement.Repository.RoleRepository;
import com.example.demo.ExpenseManagement.Security.JWTService;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ActiveStatus activeStatus;
    
    public Role getRoleById(Long roleId) {
        
        Role role = null;
        try {
            role = roleRepository.findRoleById(roleId);
            if(role == null)
                throw new ValidationException("No record found for the roleId " + roleId, HttpStatus.NOT_FOUND);
            activeStatus.isActiveOrNot(role.getIsActive());
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while retrieving role by Id "+ roleId);
        }
        return role;
    }

    public List<UserDTO> getUsersByRoleName(Long organizationId, String roleName, String authorizationHeader) {

        this.checkSameOrganization(organizationId, authorizationHeader);
        List<User> users = new ArrayList<>();
        List<UserDTO> userDTOList = new ArrayList<>();
        try {
            users = roleRepository.findUsersByRoleName(organizationId, roleName);
            
            if(users == null) {
                throw new ValidationException("No user found in the role " + roleName +" working for the organization id " + organizationId, null);
            }

            for (User user : users) {
                if(!activeStatus.isActiveOrNot(user.getIsActive())) continue;
                
                userDTOList.add(this.mapUserToUserDTO(user));
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
            role.setRoleName(role.getRoleName().toUpperCase());
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
                existingRole.setRoleName(updatedRole.getRoleName().toUpperCase());
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

    public UserDTO mapUserToUserDTO(User user) {

        UserDTO userDTO = mapper.map(user, UserDTO.class);
        userDTO.setOrganizationId(user.getOrganization().getOrganizationId());
        return userDTO;
    }

    public boolean checkSameOrganization(Long pathOrganizationId, String authHeader) {
        
        try {
            if(! pathOrganizationId.equals(jwtService.extractOrganizationId(authHeader.substring(7)))) {
                throw new ValidationException("Organization mismatch", HttpStatus.FORBIDDEN);
            }
            return true;
        }
        catch (ValidationException e) {
            throw e;
        }
    }

}
