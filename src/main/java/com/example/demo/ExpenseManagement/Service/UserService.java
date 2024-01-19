package com.example.demo.ExpenseManagement.Service;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.ExpenseManagement.DTO.UserDTO;
import com.example.demo.ExpenseManagement.Entity.User;
import com.example.demo.ExpenseManagement.ExceptionController.ApplicationException;
import com.example.demo.ExpenseManagement.ExceptionController.ValidationException;
import com.example.demo.ExpenseManagement.Repository.OrganizationRepository;
import com.example.demo.ExpenseManagement.Repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private DozerBeanMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private OrganizationService organizationService;

    public UserDTO getUserById(Long userId) {
        
        User user = null;
        try {
            user = userRepository.findUserById(userId);
            if(user == null) {
                throw new ValidationException("No record found for userId " + userId, HttpStatus.NOT_FOUND);
            }
            return this.mapUserToUserDTO(user);
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while retrieving user by Id "+ userId);
        }
    }

    public void addUser(UserDTO userDTO) {

        User user = null;
        try {
            if(userDTO == null ||
            (userDTO.getUserName() instanceof String && (userDTO.getUserName().isEmpty() || userDTO.getUserName().length() > 50)) ||
            (userDTO.getUserEmail() instanceof String && (userDTO.getUserEmail().isEmpty() || userDTO.getUserEmail().length() > 350)) ||
            (userDTO.getUserPassword() instanceof String && (userDTO.getUserPassword().isEmpty() || userDTO.getUserPassword().length() > 130)) ||
            (userDTO.getRoleId() == null) || userDTO.getOrganizationId() == null) {

                throw new ValidationException("Non null field value must not be null or empty and characters must not exceed limit.", HttpStatus.BAD_REQUEST);
            }
            user = this.mapUserDTOToUser(userDTO);
            user.setOrganization(organizationRepository.findOrganizationById(userDTO.getOrganizationId()));
            user.setRole(roleService.getRoleById(userDTO.getRoleId()));
            userRepository.saveAndFlush(user);
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while saving the user.");
        }
    }

    public void updateUser(UserDTO updatedUserDTO) {
        
        User existingUser = null;
        try {
            existingUser = userRepository.findUserById(updatedUserDTO.getUserId());

            if(existingUser == null) {
                throw new ValidationException("No record found to update for the userId " + updatedUserDTO.getUserId(), HttpStatus.NOT_FOUND);
            }

            if(updatedUserDTO.getUserName() instanceof String && ! (updatedUserDTO.getUserName().isEmpty())) {

                if(updatedUserDTO.getUserName().length() > 50)
                    throw new ValidationException("User name must not exceed 50 characters", HttpStatus.BAD_REQUEST);
                existingUser.setUserName(updatedUserDTO.getUserName());
            }

            if(updatedUserDTO.getUserEmail() instanceof String && ! (updatedUserDTO.getUserEmail().isEmpty())) {

                if(updatedUserDTO.getUserEmail().length() > 350)
                    throw new ValidationException("User email must not exceed 350 characters", HttpStatus.BAD_REQUEST);
                existingUser.setUserEmail(updatedUserDTO.getUserEmail());
            }

            if(updatedUserDTO.getUserPassword() instanceof String && ! (updatedUserDTO.getUserPassword().isEmpty())) {

                if(updatedUserDTO.getUserPassword().length() > 130)
                    throw new ValidationException("User password must not exceed 130 characters", HttpStatus.BAD_REQUEST);
                existingUser.setUserPassword(updatedUserDTO.getUserPassword());
            }

            if(updatedUserDTO.getIsActive() instanceof Boolean) {

                existingUser.setIsActive(updatedUserDTO.getIsActive());
            }

            if(updatedUserDTO.getRoleId() instanceof Long && roleService.getRoleById(updatedUserDTO.getRoleId()) != null) {

                existingUser.setRole(roleService.getRoleById(updatedUserDTO.getRoleId()));
            }

            if(updatedUserDTO.getOrganizationId() instanceof Long && organizationService.getOrganizationById(updatedUserDTO.getOrganizationId()) != null) {

                existingUser.setOrganization(organizationRepository.findOrganizationById(updatedUserDTO.getOrganizationId()));
            }
            userRepository.save(existingUser);
        }
        catch (ValidationException e) {
            throw e;
        }
        // catch (Exception e) {
        //     throw new ApplicationException("An unexpected error occurred while updating.");
        // }
    }

    public UserDTO mapUserToUserDTO(User user) {

        UserDTO userDTO = mapper.map(user, UserDTO.class);
        userDTO.setRoleId(user.getRole().getRoleId());
        userDTO.setOrganizationId(user.getOrganization().getOrganizationId());
        return userDTO;
    }

    public User mapUserDTOToUser(UserDTO userDTO) {

        User user = mapper.map(userDTO, User.class);
        user.setRole(roleService.getRoleById(userDTO.getRoleId()));
        user.setOrganization(organizationRepository.findOrganizationById(userDTO.getOrganizationId()));
        return user;
    }
}
