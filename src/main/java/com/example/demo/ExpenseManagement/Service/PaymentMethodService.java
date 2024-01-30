package com.example.demo.ExpenseManagement.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.ExpenseManagement.Entity.PaymentMethod;
import com.example.demo.ExpenseManagement.ExceptionController.ApplicationException;
import com.example.demo.ExpenseManagement.ExceptionController.ValidationException;
import com.example.demo.ExpenseManagement.Repository.PaymentMethodRepository;

@Service
public class PaymentMethodService {
    
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    public PaymentMethod getPaymentMethodById(Integer paymentMethodId) {
        
        PaymentMethod paymentMethod = null;
        try {
            paymentMethod = paymentMethodRepository.findPaymentMethodById(paymentMethodId);

            if(paymentMethod == null) {
                throw new ValidationException("No record found for the paymentMethod id " + paymentMethodId, HttpStatus.NOT_FOUND);
            }
            else if(paymentMethod.getIsActive() == false) {
                throw new ValidationException("The paymentMethod id " + paymentMethodId + " is inactive", HttpStatus.FORBIDDEN);
            }
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while retrieving payment method by Id "+ paymentMethodId);
        }
        return paymentMethod;
    }

    public void addPaymentMethod(PaymentMethod paymentMethod) {

        try {
            if(paymentMethod == null ||
                (paymentMethod.getPaymentMethodName() instanceof String && (paymentMethod.getPaymentMethodName().isEmpty() ||paymentMethod.getPaymentMethodName().length() > 20)) ||
                (paymentMethod.getPaymentMethodDescription() instanceof String && paymentMethod.getPaymentMethodDescription().length() > 300))

                throw new ValidationException("Non null field value must not be null or empty and characters must not exceed limit.", HttpStatus.BAD_REQUEST);

            paymentMethod.setIsActive(paymentMethod.getIsActive() instanceof Boolean ? (paymentMethod.getIsActive()) : (true));

            paymentMethodRepository.saveAndFlush(paymentMethod);
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while saving the payment method.");
        }
    }

    public void updatePaymentMethod(PaymentMethod updatedPaymentMethod) {

        PaymentMethod existingPaymentMethod = null;
        try {
            existingPaymentMethod = this.getPaymentMethodById(updatedPaymentMethod.getPaymentMethodId());

            if(updatedPaymentMethod.getPaymentMethodName() instanceof String && ! updatedPaymentMethod.getPaymentMethodName().isEmpty()) {

                if(updatedPaymentMethod.getPaymentMethodName().length() > 20) {
                    throw new ValidationException("Payment method name must not exceed 20 characters", HttpStatus.BAD_REQUEST);
                }
                existingPaymentMethod.setPaymentMethodName(updatedPaymentMethod.getPaymentMethodName());
            }

            if(updatedPaymentMethod.getPaymentMethodDescription() instanceof String) {

                if(updatedPaymentMethod.getPaymentMethodDescription().length() > 300) {
                    throw new ValidationException("Payment method description must not exceed 300 characters", HttpStatus.BAD_REQUEST);
                }
                existingPaymentMethod.setPaymentMethodDescription(updatedPaymentMethod.getPaymentMethodDescription());
            }

            if(updatedPaymentMethod.getIsActive() instanceof Boolean) {
                existingPaymentMethod.setIsActive(updatedPaymentMethod.getIsActive());
            }
            paymentMethodRepository.saveAndFlush(existingPaymentMethod);
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while updating.");
        }
    }
}
