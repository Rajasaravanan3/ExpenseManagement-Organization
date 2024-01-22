package com.example.demo.ExpenseManagement.Service;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

import com.example.demo.ExpenseManagement.DTO.ExpenseDTO;
import com.example.demo.ExpenseManagement.Entity.Budget;
import com.example.demo.ExpenseManagement.Entity.Expense;
import com.example.demo.ExpenseManagement.Entity.User;
import com.example.demo.ExpenseManagement.ExceptionController.ApplicationException;
import com.example.demo.ExpenseManagement.ExceptionController.ValidationException;
import com.example.demo.ExpenseManagement.Repository.BudgetRepository;
import com.example.demo.ExpenseManagement.Repository.CategoryRepository;
import com.example.demo.ExpenseManagement.Repository.ExpenseRepository;
import com.example.demo.ExpenseManagement.Repository.UserRepository;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private PaymentMethodService paymentMethodService;

    @Autowired
    private BudgetRepository budgetRepository;
    
    public ExpenseDTO getExpenseById(Long expenseId) {
        
        Expense expense = null;
        try {
            expense = expenseRepository.findExpenseById(expenseId);

            if(expense == null) {
                throw new ValidationException("No record found for the expense id "+ expenseId, HttpStatus.NOT_FOUND);
            }
            return this.mapExpenseToExpenseDTO(expense);
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while retrieving expense by Id "+ expenseId);
        }
    }

    public String addExpense(ExpenseDTO expenseDTO) {

        Expense expense = null;
        String notification = "";
        try {
            if(expenseDTO == null || expenseDTO.getCurrencyId() instanceof Integer || expenseDTO.getUserId() instanceof Long ||
                expenseDTO.getPaymentMethodId() instanceof Integer || expenseDTO.getCategoryId() instanceof Long ||
                (expenseDTO.getAmount() instanceof BigDecimal && validateBigDecimal(expenseDTO.getAmount(), 10, 2)) ||
                expenseDTO.getExpenseDate() instanceof ZonedDateTime ||
                (expenseDTO.getExpenseDescription() instanceof String && (expenseDTO.getExpenseDescription().isEmpty() || expenseDTO.getExpenseDescription().length() > 1000)))

                throw new ValidationException("Non null field value must not be null or empty and characters must not exceed limit.", HttpStatus.BAD_REQUEST);

            expense = this.mapExpenseDTOToExpense(expenseDTO);
            
            notification = this.validateExpenseAmount(expenseDTO.getCategoryId(), BigDecimal.ZERO, expenseDTO.getAmount());
                
                if(!notification.isEmpty())
                    return notification;

            expenseRepository.saveAndFlush(expense);
            return "Expense added successfully";
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while saving expense");
        }
    }

    public String updateExpense(Long expenseId, String approvalStatus) {
        
        Expense existingExpense = null;
        String notification = "";
        try {
            existingExpense = expenseRepository.findExpenseById(expenseId);
            if(existingExpense == null) {
                throw new ValidationException("No record found to update for the expense id " + expenseId, HttpStatus.NOT_FOUND);
            }
            if(approvalStatus instanceof String && !(approvalStatus.isEmpty() || approvalStatus.length() > 15)) {
                existingExpense.setApprovalStatus(approvalStatus);
            }
            else {
                throw new ValidationException("Invalid approval status", HttpStatus.BAD_REQUEST);
            }
            // if(updatedExpenseDTO.getAmount() instanceof BigDecimal && validateBigDecimal(updatedExpenseDTO.getAmount(), 10, 2)) {
            //     existingExpense.setAmount(updatedExpenseDTO.getAmount());

            //     notification = this.validateExpenseAmount(updatedExpenseDTO.getCategoryId(), existingExpense.getAmount(), updatedExpenseDTO.getAmount());
                
            //     if(!notification.isEmpty())
            //         return notification;
            // }

            // if(updatedExpenseDTO.getExpenseDate() instanceof ZonedDateTime) {
            //     existingExpense.setExpenseDate(updatedExpenseDTO.getExpenseDate());
            // }
            // if(updatedExpenseDTO.getExpenseDescription() instanceof String && !updatedExpenseDTO.getExpenseDescription().isEmpty()) {
            //     if(updatedExpenseDTO.getExpenseDescription().length() > 1000) {
            //         throw new ValidationException("Expense description must not exceed 1000 characters", HttpStatus.BAD_REQUEST);
            //     }
            //     existingExpense.setExpenseDescription(updatedExpenseDTO.getExpenseDescription());
            // }
            // if(updatedExpenseDTO.getCategoryId() instanceof Long) {
            //     existingExpense.setCategory(categoryRepository.findCategoryById(updatedExpenseDTO.getCategoryId()));
            // }
            // if(updatedExpenseDTO.getCurrencyId() instanceof Integer) {
            //     existingExpense.setCurrency(currencyService.getCurrencyById(updatedExpenseDTO.getCurrencyId()));
            // }
            // if(updatedExpenseDTO.getPaymentMethodId() instanceof Integer) {
            //     existingExpense.setPaymentMethod(paymentMethodService.getPaymentMethodById(updatedExpenseDTO.getPaymentMethodId()));
            // }
            // if(updatedExpenseDTO.getUserId() instanceof Long) {
            //     existingExpense.setUser(userRepository.findUserById(updatedExpenseDTO.getUserId()));
            // }
            expenseRepository.save(existingExpense);
            return "Updated Successfully";
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while updating expense.");
        }
    }

    public void deleteExpense(long expenseId, Long userId) {

        Expense existingExpense = null;
        try {
            existingExpense = expenseRepository.findExpenseById(expenseId);
            if(this.getExpenseById(expenseId) != null) {
                expenseRepository.deleteById(expenseId);
            }
        }
        catch(Exception e) {
            throw new ApplicationException("An unexpected error occured while deleting the expense id " + expenseId);
        }
    }

    //Common method for filter functionality
    public List<ExpenseDTO> getExpenses(List<Expense> expenseList, String notFoundMessage) {
        
        List<ExpenseDTO> expenseDtoList = new ArrayList<>();

        try {
            if(expenseList == null) {
                throw new ValidationException(notFoundMessage, HttpStatus.NOT_FOUND);
            }
        }
        catch(ValidationException e) {
            throw e;
        }

        for(Expense expense : expenseList) {
            expenseDtoList.add(this.mapExpenseToExpenseDTO(expense));
        }
        return expenseDtoList;
    }

    public ExpenseDTO mapExpenseToExpenseDTO(Expense expense) {

        ExpenseDTO expenseDTO = mapper.map(expense, ExpenseDTO.class);
        expenseDTO.setCategoryId(expense.getCategory().getCategoryId());
        expenseDTO.setCurrencyId(expense.getCurrency().getCurrencyId());
        expenseDTO.setPaymentMethodId(expense.getPaymentMethod().getPaymentMethodId());
        expenseDTO.setUserId(expense.getUser().getUserId());
        return expenseDTO;
    }

    public Expense mapExpenseDTOToExpense(ExpenseDTO expenseDTO) {
        
        Expense expense = mapper.map(expenseDTO, Expense.class);
        expense.setCategory(categoryRepository.findCategoryById(expenseDTO.getCategoryId()));
        expense.setCurrency(currencyService.getCurrencyById(expenseDTO.getCurrencyId()));
        expense.setPaymentMethod(paymentMethodService.getPaymentMethodById(expenseDTO.getPaymentMethodId()));
        expense.setUser(userRepository.findUserById(expenseDTO.getUserId()));
        return expense;
    }

    public boolean validateBigDecimal(BigDecimal bigDecimal, int range, int precision) {

        String str = bigDecimal.toString();
        String arr[] = str.split(".");

        if(arr[0].length() <= range-precision && (arr.length >= 1 && arr[1].length() <= precision)) {
            return true;
        }
        return false;
    }

    public String validateExpenseAmount(Long categoryId, BigDecimal currentBudget, BigDecimal updatedBudget) {

        String message = "";
        List<Budget> budgets = budgetRepository.getBudgetsByCategory(categoryId);

        BigDecimal lastOneWeekExpenses = updatedBudget.add(this.getSumOfLastSevenDaysExpenses(categoryId)).subtract(currentBudget);
        BigDecimal lastOneMonthExpenses = updatedBudget.add(this.getSumOfLastOneMonthExpenses(categoryId)).subtract(currentBudget);
        BigDecimal lastOneYearExpenses = updatedBudget.add(this.getSumOfLastOneYearExpenses(categoryId)).subtract(currentBudget);

        for (Budget budget : budgets) {
                
            if(String.valueOf(budget.getBudgetType()).equalsIgnoreCase("YEARLY") && budget.getBudgetAmount().compareTo(lastOneYearExpenses) < 0) {
                message = "You can't place this expense as it exceeds annual budget limit for this category id " + categoryId;
                break;
            }
            else if(String.valueOf(budget.getBudgetType()).equalsIgnoreCase("MONTHLY") && budget.getBudgetAmount().compareTo(lastOneMonthExpenses) < 0) {
                message = "You can't place this expense as it exceeds monthly budget limit for this category id " + categoryId;
                break;
            }
            else if(String.valueOf(budget.getBudgetType()).equalsIgnoreCase("WEEKLY") && budget.getBudgetAmount().compareTo(lastOneWeekExpenses) < 0) {
                message = "You can't place this expense as it exceeds weekly budget limit for this category id " + categoryId;
                break;
            }
        }

        return message;
    }

    public BigDecimal getSumOfLastSevenDaysExpenses(Long categoryId) {

        return expenseRepository.findSumOfLastSevenDaysExpenses(categoryId);
    }

    public BigDecimal getSumOfLastOneMonthExpenses(Long categoryId) {

        return expenseRepository.findSumOfLastOneMonthExpenses(categoryId);
    }

    public BigDecimal getSumOfLastOneYearExpenses(Long categoryId) {

        return expenseRepository.findSumOfLastOneYearExpenses(categoryId);
    }

    //filters
    public List<ExpenseDTO> getByAmountSpentHigherToLower() {
        
        List<Expense> expenseList = null;
        List<ExpenseDTO> expenseDTOList = null;
        try {
            expenseList = expenseRepository.findByAmount();
            
            return this.getExpenses(expenseList, "No expenses found");
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while getting expenses");
        }
    }

    public List<ExpenseDTO> getByCategoryName(String categoryName) {

        List<Expense> expenseList = null;

        try {
            expenseList = expenseRepository.findByCategoryName(categoryName);

            String notFoundMessage = "No expense found for the category name " + categoryName;
            return this.getExpenses(expenseList, notFoundMessage);
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while retrieving expenses for category name " + categoryName);
        }
    }

    public List<ExpenseDTO> getByCurrencyCode(String currencyCode) {

        List<Expense> expenseList = null;
        try {
            expenseList = expenseRepository.findByCurrencyCode(currencyCode);

            String notFoundMessage = "No expense found for the currency code " + currencyCode;
            return this.getExpenses(expenseList, notFoundMessage);
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while retrieving expenses for currencyCode " + currencyCode);
        }
    }

    public List<ExpenseDTO> getByPaymentMethodName(String paymentMethodName) {

        List<Expense> expenseList = null;
        try {
            expenseList = expenseRepository.findByPaymentMethodName(paymentMethodName);

            String notFoundMessage = "No expense found for the paymentMethod Name " + paymentMethodName;
            return this.getExpenses(expenseList, notFoundMessage);
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while retrieving expenses for paymentMethod Name " + paymentMethodName);
        }
    }


    // admin's access
    public List<ExpenseDTO> getExpensesByUserId(Long adminId, Long userId) {

        User user = null;
        List<Expense> expenseList = null;
        try {
            user = userRepository.findUserById(userId);

            if(user != null && user.getRole().getRoleName().equalsIgnoreCase("admin")) {

                expenseList = expenseRepository.findAllExpensesByUser(userId);
            }
            else{
                throw new ValidationException("Access denied", HttpStatus.FORBIDDEN);
            }
            String notFoundMessage = "No expense found for the user id " + userId;
            return this.getExpenses(expenseList, notFoundMessage);
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while retrieving expenses by user id " + userId);
        }
    }
    //admin can update category, approver, budget
}
