package com.example.demo.ExpenseManagement.Service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.example.demo.ExpenseManagement.ExceptionController.ValidationException;

@Component
public class ActiveStatus {
    
    public boolean isActiveOrNot(Boolean isActive) {
        
        try {
            if(isActive) {
                return true;
            }
            throw new ValidationException("Not active", HttpStatus.FORBIDDEN);
        }
        catch (ValidationException e) {
            throw e;
        }
    }
}
