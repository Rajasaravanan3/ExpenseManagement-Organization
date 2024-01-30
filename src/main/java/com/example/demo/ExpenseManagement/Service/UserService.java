package com.example.demo.ExpenseManagement.Service;


import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import org.modelmapper.ModelMapper;
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
    private ModelMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private OrganizationService organizationService;
    
    Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    Pattern hashedPasswordPattern = Pattern.compile("^[a-zA-Z0-9+/]{8,}$");
    Pattern usernamePattern = Pattern.compile("^[A-Za-z]+[ ]+[A-Za-z]+$");
    Matcher matcher = null;

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

    public List<UserDTO> getUsersByRoleName(Long organizationId, String roleName) {
        List<User> users = new ArrayList<>();
        List<UserDTO> userDTOList = new ArrayList<>();
        try {
            users = userRepository.getusersByRoleName(organizationId, roleName);
            
            if(users == null) {
                throw new ValidationException("No user found in the role " + roleName +" working for the organization id " + organizationId, null);
            }

            for (User user : users) {
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

    public UserDTO getUserByEmail(String userEmail) {
        User user = null;
        try {
            user = userRepository.findByUserEmail(userEmail);
            return this.mapUserToUserDTO(user);
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occured while retrieving user with email " + userEmail);
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
            matcher = usernamePattern.matcher(userDTO.getUserName());
            boolean validName = matcher.find();
            boolean validEmail = emailPattern.matcher(userDTO.getUserEmail()).find();
            boolean validPassword = hashedPasswordPattern.matcher(userDTO.getUserPassword()).find();
            if(!validName) {
                throw new ValidationException("Invalid username", HttpStatus.BAD_REQUEST);
            }
            else if(!validEmail) {
                throw new ValidationException("Invalid email address", HttpStatus.BAD_REQUEST);
            }
            else if(!validPassword) {
                throw new ValidationException("Invalid password", HttpStatus.BAD_REQUEST);
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
                if(!usernamePattern.matcher(updatedUserDTO.getUserName()).find()) {
                    throw new ValidationException("Invalid username", HttpStatus.BAD_REQUEST);
                }
                existingUser.setUserName(updatedUserDTO.getUserName());
            }

            if(updatedUserDTO.getUserEmail() instanceof String && ! (updatedUserDTO.getUserEmail().isEmpty())) {

                if(updatedUserDTO.getUserEmail().length() > 350)
                    throw new ValidationException("User email must not exceed 350 characters", HttpStatus.BAD_REQUEST);
                if(!emailPattern.matcher(updatedUserDTO.getUserEmail()).find()) {
                    throw new ValidationException("Invalid Email address", HttpStatus.BAD_REQUEST);
                }
                existingUser.setUserEmail(updatedUserDTO.getUserEmail());
            }

            if(updatedUserDTO.getUserPassword() instanceof String && ! (updatedUserDTO.getUserPassword().isEmpty())) {

                if(updatedUserDTO.getUserPassword().length() > 130)
                    throw new ValidationException("User password must not exceed 130 characters", HttpStatus.BAD_REQUEST);
                if(!hashedPasswordPattern.matcher(updatedUserDTO.getUserPassword()).find()) {
                    throw new ValidationException("Invalid password", HttpStatus.BAD_REQUEST);
                }
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
            userRepository.saveAndFlush(existingUser);
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while updating.");
        }
    }

    public List<UserDTO> getAllUsers(Long organizationId) {

        List<User> users = new ArrayList<>();
        List<UserDTO> userDTOList = new ArrayList<>();
        try {
            users = userRepository.findAllUsers(organizationId);
            for (User user : users) {
                userDTOList.add(this.mapUserToUserDTO(user));
            }

            return userDTOList;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while retrieving all users working for the organization" + organizationId);
        }
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
