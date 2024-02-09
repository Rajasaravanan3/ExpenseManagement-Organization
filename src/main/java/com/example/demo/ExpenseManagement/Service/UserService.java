package com.example.demo.ExpenseManagement.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.ExpenseManagement.DTO.RoleUpdate;
import com.example.demo.ExpenseManagement.DTO.UserDTO;
import com.example.demo.ExpenseManagement.Entity.Role;
import com.example.demo.ExpenseManagement.Entity.User;
import com.example.demo.ExpenseManagement.ExceptionController.ApplicationException;
import com.example.demo.ExpenseManagement.ExceptionController.ValidationException;
import com.example.demo.ExpenseManagement.Repository.OrganizationRepository;
import com.example.demo.ExpenseManagement.Repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
    
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ActiveStatus activeStatus;

    UserService() {}
    
    Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    Pattern hashedPasswordPattern = Pattern.compile("^[a-zA-Z0-9+/]{8,}$");
    Pattern fullNamePattern = Pattern.compile("^[A-Za-z]+[ ]+[A-Za-z]+$");
    Matcher matcher = null;

    public UserDTO getUserById(Long userId) {
        
        User user = null;
        try {
            user = userRepository.findUserById(userId);
            if(user == null) {
                throw new ValidationException("No record found for userId " + userId, HttpStatus.NOT_FOUND);
            }
            activeStatus.isActiveOrNot(user.getIsActive());
            return this.mapUserToUserDTO(user);
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while retrieving user by Id "+ userId);
        }
    }

    public UserDTO getUserByUsername(String username) {
        User user = null;
        try {
            user = userRepository.findByUsername(username);
            activeStatus.isActiveOrNot(user.getIsActive());
            return this.mapUserToUserDTO(user);
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occured while retrieving user with email " + username);
        }
    }

    public void updateUser(UserDTO updatedUserDTO) {
        
        User existingUser = null;
        try {
            existingUser = userRepository.findUserById(updatedUserDTO.getUserId());

            if(existingUser == null) {
                throw new ValidationException("No record found to update for the userId " + updatedUserDTO.getUserId(), HttpStatus.NOT_FOUND);
            }

            if(updatedUserDTO.getFullName() instanceof String && ! (updatedUserDTO.getFullName().isEmpty())) {

                if(updatedUserDTO.getFullName().length() > 50)
                    throw new ValidationException("User name must not exceed 50 characters", HttpStatus.BAD_REQUEST);
                if(!fullNamePattern.matcher(updatedUserDTO.getFullName()).find()) {
                    throw new ValidationException("Invalid username", HttpStatus.BAD_REQUEST);
                }
                existingUser.setFullName(updatedUserDTO.getFullName());
            }

            if(updatedUserDTO.getUsername() instanceof String && ! (updatedUserDTO.getUsername().isEmpty())) {

                if(updatedUserDTO.getUsername().length() > 350)
                    throw new ValidationException("User email must not exceed 350 characters", HttpStatus.BAD_REQUEST);
                if(!emailPattern.matcher(updatedUserDTO.getUsername()).find()) {
                    throw new ValidationException("Invalid Email address", HttpStatus.BAD_REQUEST);
                }
                existingUser.setUsername(updatedUserDTO.getUsername());
            }

            if(updatedUserDTO.getPassword() instanceof String && ! (updatedUserDTO.getPassword().isEmpty())) {

                if(updatedUserDTO.getPassword().length() > 130)
                    throw new ValidationException("User password must not exceed 130 characters", HttpStatus.BAD_REQUEST);
                if(!hashedPasswordPattern.matcher(updatedUserDTO.getPassword()).find()) {
                    throw new ValidationException("Invalid password", HttpStatus.BAD_REQUEST);
                }
                existingUser.setPassword(updatedUserDTO.getPassword());
            }

            if(updatedUserDTO.getIsActive() instanceof Boolean) {

                existingUser.setIsActive(updatedUserDTO.getIsActive());
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
                if(!(user.getIsActive())) continue;

                userDTOList.add(this.mapUserToUserDTO(user));
            }

            return userDTOList;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while retrieving all users working for the organization" + organizationId);
        }
    }

    public void addRoleToUser(RoleUpdate roleUpdate) {

        try {
            if(roleUpdate.getUserId() == null) {
                throw new ValidationException("UserId must not be null", HttpStatus.BAD_REQUEST);
            }
            if(roleUpdate.getRoleId() == null) {
                throw new ValidationException("roleId must not be null", HttpStatus.BAD_REQUEST);
            }
            this.getUserById(roleUpdate.getUserId());
            Role role = roleService.getRoleById(roleUpdate.getRoleId());
            User user = userRepository.findUserById(roleUpdate.getUserId());
            Set<Role> roles = user.getRoles();
            for (Role r : roles) {
                if(r.getRoleName() == role.getRoleName()) {
                    throw new ValidationException("User already holds " + role.getRoleName() + " role.", HttpStatus.CONFLICT);
                }
            }
            roles.add(role);
            user.setRoles(roles);
            userRepository.save(user);
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occured occured while updating role to user " + roleUpdate.getUserId());
        }
    }

    public void removeRoleFromUser(RoleUpdate roleUpdate) {
        
        try {
            if(roleUpdate.getUserId() == null) {
                throw new ValidationException("UserId must not be null", HttpStatus.BAD_REQUEST);
            }
            if(roleUpdate.getRoleId() == null) {
                throw new ValidationException("roleId must not be null", HttpStatus.BAD_REQUEST);
            }
            this.getUserById(roleUpdate.getUserId());
            Role role = roleService.getRoleById(roleUpdate.getRoleId());
            User user = userRepository.findUserById(roleUpdate.getUserId());
            Set<Role> roles = user.getRoles();
            for (Role r : roles) {
                if(r.getRoleName() == role.getRoleName()) {
                    roles.remove(role);
                    user.setRoles(roles);
                    userRepository.save(user);
                    return;
                }
            }
            throw new ValidationException("User don't have " + role.getRoleName() + " role.", HttpStatus.NOT_FOUND);
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occured occured while removing role from user " + roleUpdate.getUserId());
        }
    }

    public UserDTO mapUserToUserDTO(User user) {

        UserDTO userDTO = mapper.map(user, UserDTO.class);
        userDTO.setOrganizationId(user.getOrganization().getOrganizationId());
        return userDTO;
    }

    public User mapUserDTOToUser(UserDTO userDTO) {

        User user = mapper.map(userDTO, User.class);
        user.setOrganization(organizationRepository.findOrganizationById(userDTO.getOrganizationId()));
        return user;
    }

    private PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        user.setPassword(encoder.encode(user.getPassword()));
        return user;
    }
}
