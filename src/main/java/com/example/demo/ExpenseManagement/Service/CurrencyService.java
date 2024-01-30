package com.example.demo.ExpenseManagement.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.ExpenseManagement.Entity.Currency;
import com.example.demo.ExpenseManagement.ExceptionController.ApplicationException;
import com.example.demo.ExpenseManagement.ExceptionController.ValidationException;
import com.example.demo.ExpenseManagement.Repository.CurrencyRepository;

@Service
public class CurrencyService {
    
    @Autowired
    private CurrencyRepository currencyRepository;

    public Currency getCurrencyById(Integer currencyId) {
        
        Currency currency = null;
        try {
            currency = currencyRepository.findCurrencyById(currencyId);
            if(currency == null) {
                throw new ValidationException("No record found for the currencyId " + currencyId, HttpStatus.NOT_FOUND);
            }
            if(currency.getIsActive() == false) {
                throw new ValidationException("The currency id " + currencyId + " is inactive", HttpStatus.FORBIDDEN);
            }
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while retrieving currency by Id "+ currencyId);
        }
        return currency;
    }

    public void addCurrency(Currency currency) {
        
        try {
            if(currency == null ||
                (currency.getCurrencyCode() instanceof String && (currency.getCurrencyCode().isEmpty()) || currency.getCurrencyCode().length() > 3) ||
                (currency.getCurrencyName() instanceof String && (currency.getCurrencyName().isEmpty() || currency.getCurrencyName().length() > 30)))

                    throw new ValidationException("Non null field value must not be null or empty and characters must not exceed limit.", HttpStatus.BAD_REQUEST);

            currency.setIsActive(currency.getIsActive() instanceof Boolean ? currency.getIsActive() : true);
            
            currencyRepository.saveAndFlush(currency);
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while saving the currency.");
        }
    }

    public void updateCurrency(Currency updatedCurrency) {

        Currency existingCurrency = null;
        try {
            existingCurrency = this.getCurrencyById(updatedCurrency.getCurrencyId());
            
            if(updatedCurrency.getCurrencyCode() instanceof String && ! updatedCurrency.getCurrencyCode().isEmpty()) {

                if(updatedCurrency.getCurrencyCode().length() > 3) {
                    throw new ValidationException("Currency code must not exceed 3 characters", HttpStatus.BAD_REQUEST);
                }
                existingCurrency.setCurrencyCode(updatedCurrency.getCurrencyCode());
            }

            if(updatedCurrency.getCurrencyName() instanceof String && ! updatedCurrency.getCurrencyName().isEmpty()) {

                if(updatedCurrency.getCurrencyName().length() > 30) {
                    throw new ValidationException("Currency name must not exceed 30 characters", HttpStatus.BAD_REQUEST);
                }
                existingCurrency.setCurrencyName(updatedCurrency.getCurrencyName());
            }
            
            currencyRepository.saveAndFlush(existingCurrency);
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while updating.");
        }
    }
}
