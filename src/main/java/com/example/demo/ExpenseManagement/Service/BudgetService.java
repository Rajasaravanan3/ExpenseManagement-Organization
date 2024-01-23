package com.example.demo.ExpenseManagement.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.ZoneId;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.ExpenseManagement.DTO.BudgetDTO;
import com.example.demo.ExpenseManagement.Entity.Budget;
import com.example.demo.ExpenseManagement.ExceptionController.ApplicationException;
import com.example.demo.ExpenseManagement.ExceptionController.ValidationException;
import com.example.demo.ExpenseManagement.Repository.BudgetRepository;
import com.example.demo.ExpenseManagement.Repository.CategoryRepository;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CategoryRepository categoryRepository;
    
    public BudgetDTO getBudgetById(Long budgetId) {
        
        Budget budget = null;
        try {
            budget = budgetRepository.findBudgetById(budgetId);
            if(budget == null) {
                throw new ValidationException("No record found for the budget id " + budgetId, HttpStatus.NOT_FOUND);
            }
            return this.mapBudgetToBudgetDTO(budget);
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while retrieving budget by Id "+ budgetId);
        }
    }

    public void addBudget(BudgetDTO budgetDTO) {        // budgetType string to enum

        Budget budget = null;
        try {
            if(budgetDTO == null ||
                (budgetDTO.getBudgetAmount() == null && this.validateBigDecimal(budgetDTO.getBudgetAmount(),10, 2)) ||
                budgetDTO.getBudgetType() instanceof String && (budgetDTO.getBudgetType().isEmpty() || budgetDTO.getBudgetType().length() > 30) ||
                budgetDTO.getCategoryId() == null) {
                    
                    throw new ValidationException("Non null field value must not be null or empty and characters must not exceed limit.", HttpStatus.BAD_REQUEST);
            }
            budget = this.mapBudgetDTOToBudget(budgetDTO);
            budgetRepository.saveAndFlush(budget);
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while saving budget.");
        }
    }

    public void updateBudget(BudgetDTO updatedBudgetDTO) {

        Budget existingBudget = null;
        try {
            existingBudget = budgetRepository.findBudgetById(updatedBudgetDTO.getBudgetId());
            if(existingBudget == null) {
                throw new ValidationException("No record found to update for the budget id " + updatedBudgetDTO.getBudgetId(), HttpStatus.NOT_FOUND);
            }

            if(updatedBudgetDTO.getBudgetType() instanceof String || ! (updatedBudgetDTO.getBudgetType().isEmpty())) {
                existingBudget.setBudgetType(updatedBudgetDTO.getBudgetType());
            }

            if(updatedBudgetDTO.getBudgetAmount() instanceof BigDecimal && this.validateBigDecimal(updatedBudgetDTO.getBudgetAmount(), 10, 2)) {
                existingBudget.setBudgetAmount(updatedBudgetDTO.getBudgetAmount());
            }

            if(updatedBudgetDTO.getIsActive() instanceof Boolean) {
                existingBudget.setIsActive(updatedBudgetDTO.getIsActive());
            }

            existingBudget.setModifiedTime(ZonedDateTime.now(ZoneId.of("UTC")));

            if(updatedBudgetDTO.getCategoryId() instanceof Long) {
                existingBudget.setCategory(categoryRepository.findCategoryById(updatedBudgetDTO.getCategoryId()));
            }
            budgetRepository.saveAndFlush(existingBudget);
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while updating budget.");
        }
    }

    public BudgetDTO mapBudgetToBudgetDTO(Budget budget) {

        BudgetDTO budgetDTO = mapper.map(budget, BudgetDTO.class);
        budgetDTO.setCategoryId(budget.getCategory().getCategoryId());

        if(budget.getCreatedTime() instanceof ZonedDateTime) {
            ZonedDateTime createdTime = budget.getCreatedTime();
            budgetDTO.setCreatedTime(createdTime.withZoneSameInstant(ZoneId.systemDefault()));
        }
        if(budget.getModifiedTime() instanceof ZonedDateTime) {
            ZonedDateTime modifiedTime = budget.getModifiedTime();
            budgetDTO.setModifiedTime(modifiedTime.withZoneSameInstant(ZoneId.systemDefault()));
        }
        return budgetDTO;
    }

    public Budget mapBudgetDTOToBudget(BudgetDTO budgetDTO) {

        Budget budget = mapper.map(budgetDTO, Budget.class);
        budget.setCategory(categoryRepository.findCategoryById(budgetDTO.getCategoryId()));
        budget.setCreatedTime(ZonedDateTime.now().withZoneSameInstant(ZoneId.systemDefault()));
        budget.setModifiedTime(ZonedDateTime.now(ZoneId.of("UTC")));

        if(budget.getIsActive() == null) {
            budget.setIsActive(true);
        }
        return budget;
    }

    public boolean validateBigDecimal(BigDecimal bigDecimal,int range,int precision) {

        String str = bigDecimal.toString();
        String arr[] = str.split("\\.");
        if(arr[0].length() <= range-precision && (arr.length >= 1 && arr[1].length() <= precision)) {
            return true;
        }
        return false;
    }
}
