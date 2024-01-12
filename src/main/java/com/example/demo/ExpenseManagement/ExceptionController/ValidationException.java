
package com.example.demo.ExpenseManagement.ExceptionController;

import org.springframework.http.HttpStatus;

public class ValidationException extends RuntimeException{
    
    private String message;
    private HttpStatus httpStatus;

    public ValidationException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
    
    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
