package com.example.demo.ExpenseManagement.Security;

import com.example.demo.ExpenseManagement.DTO.JwtAuthenticationResponse;
import com.example.demo.ExpenseManagement.DTO.RefreshTokenRequest;
import com.example.demo.ExpenseManagement.DTO.SignInRequest;
import com.example.demo.ExpenseManagement.DTO.SignUpRequest;
import com.example.demo.ExpenseManagement.Repository.OrganizationRepository;
import com.example.demo.ExpenseManagement.Repository.UserRepository;
import com.example.demo.ExpenseManagement.Entity.Organization;
import com.example.demo.ExpenseManagement.Entity.User;
import com.example.demo.ExpenseManagement.ExceptionController.ApplicationException;
import com.example.demo.ExpenseManagement.ExceptionController.ValidationException;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // @Autowired
    // private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    public User signUp(SignUpRequest signUpRequest) {
        
        User user = new User();
        try {
            if(signUpRequest == null) {
                throw new ValidationException("User details must not be empty.", HttpStatus.BAD_REQUEST);
            }
            if(signUpRequest.getFullName() instanceof String || signUpRequest.getFullName().isEmpty() || signUpRequest.getFullName().length() > 50) {
                throw new ValidationException("fullName must not be null or exceed limit", HttpStatus.BAD_REQUEST);
            }
            if(signUpRequest.getUsername() == null || signUpRequest.getUsername().isEmpty() || signUpRequest.getUsername().length() > 350) {
                throw new ValidationException("username must not be null or exceed limit", HttpStatus.BAD_REQUEST);
            }
            if(signUpRequest.getPassword() == null || signUpRequest.getPassword().isEmpty() || signUpRequest.getPassword().length() > 130) {
                throw new ValidationException("password must not be null or exceed limit", HttpStatus.BAD_REQUEST);
            }
            if(signUpRequest.getOrganizationId() == null) {
                throw new ValidationException("organizationId must not be null", HttpStatus.BAD_REQUEST);
            }

            Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
            Pattern hashedPasswordPattern = Pattern.compile("^[a-zA-Z0-9+/]{8,}$");
            Pattern fullNamePattern = Pattern.compile("^[A-Za-z]+[ ]+[A-Za-z]+$");
            Matcher matcher = null;

            matcher = fullNamePattern.matcher(signUpRequest.getFullName());
            boolean validName = matcher.find();
            boolean validEmail = emailPattern.matcher(signUpRequest.getUsername()).find();
            boolean validPassword = hashedPasswordPattern.matcher(signUpRequest.getPassword()).find();
            if(!validName) {
                throw new ValidationException("Invalid username", HttpStatus.BAD_REQUEST);
            }
            else if(!validEmail) {
                throw new ValidationException("Invalid email address", HttpStatus.BAD_REQUEST);
            }
            else if(!validPassword) {
                throw new ValidationException("Invalid password", HttpStatus.BAD_REQUEST);
            }

            user.setFullName(signUpRequest.getFullName());
            user.setUsername(signUpRequest.getUsername());
            user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
            Organization organization = organizationRepository.findOrganizationById(signUpRequest.getOrganizationId());
            if(organization == null) {
                throw new ValidationException("organization not found for id " + signUpRequest.getOrganizationId(), HttpStatus.NOT_FOUND);
            }
            user.setOrganization(organization);
            user.setIsActive(true);

            // only admin can set role to user

            return userRepository.saveAndFlush(user);
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occured while trying to signup");
        }
    }

    public JwtAuthenticationResponse signIn(SignInRequest signInRequest) {

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        try {
            if(signInRequest == null ||
            (signInRequest.getUsername() instanceof String && (signInRequest.getUsername().isEmpty() || signInRequest.getUsername().length() > 350)) ||
            (signInRequest.getPassword() instanceof String && (signInRequest.getPassword().isEmpty() || signInRequest.getPassword().length() > 130))) {
                throw new ValidationException("credentials cannot be null or exceed limit", HttpStatus.BAD_REQUEST);
            }

            User user = userRepository.findByUsername(signInRequest.getUsername());
            if (user == null || !user.getPassword().equals(signInRequest.getPassword())) {
                throw new ValidationException("Invalid email or password", HttpStatus.UNAUTHORIZED);
            }

            // authenticationManager.authenticate(
            // new UsernamePasswordAuthenticationToken(signInRequest.getUsername(),
            // signInRequest.getPassword()));

            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

            
            jwtAuthenticationResponse.setAccessToken(accessToken);
            jwtAuthenticationResponse.setRefreshToken(refreshToken);
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occured while trying to signin");
        }
        return jwtAuthenticationResponse;
    }

    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshToken) {

        try {
            String username = jwtService.extractUserName(refreshToken.getRefreshToken());
            User user = userRepository.findByUsername(username);
            if(user == null) {
                throw new ValidationException("Invalid refresh token", HttpStatus.UNAUTHORIZED);
            }
            
            if(jwtService.isTokenValid(refreshToken.getRefreshToken(), user)) {
                
                String accessToken = jwtService.generateAccessToken(user);
                JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
                jwtAuthenticationResponse.setAccessToken(accessToken);
                jwtAuthenticationResponse.setRefreshToken(refreshToken.getRefreshToken());
                return jwtAuthenticationResponse;
            }
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occured while trying to refresh token");
        }
        return null;
    }

}
