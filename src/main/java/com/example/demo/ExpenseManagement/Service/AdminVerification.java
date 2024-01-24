package com.example.demo.ExpenseManagement.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.example.demo.ExpenseManagement.Entity.User;
import com.example.demo.ExpenseManagement.ExceptionController.ApplicationException;
import com.example.demo.ExpenseManagement.ExceptionController.ValidationException;
import com.example.demo.ExpenseManagement.Repository.UserRepository;

@Component
public class AdminVerification {
    
    @Autowired
    private UserRepository userRepository;

    public boolean isAdmin(Long adminId) {
        User admin = null;
        try {
            admin = userRepository.findUserById(adminId);
            if(admin != null && admin.getRole().getRoleName().equalsIgnoreCase("admin")) {
                return true;
            }
            else {
                throw new ValidationException("Access denied", HttpStatus.FORBIDDEN);
            }
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occured while verifying admin");
        }
    }
}
