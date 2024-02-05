package com.example.demo.ExpenseManagement.Service;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.example.demo.ExpenseManagement.DTO.ApprovalsDTO;
import com.example.demo.ExpenseManagement.DTO.ExpenseDTO;
import com.example.demo.ExpenseManagement.Entity.ApprovalStatus;
import com.example.demo.ExpenseManagement.Entity.Budget;
import com.example.demo.ExpenseManagement.Entity.Expense;
import com.example.demo.ExpenseManagement.Entity.Role;
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
    private CategoryService categoryService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private PaymentMethodService paymentMethodService;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private ApprovalsService approvalsService;
    
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
            if(expenseDTO == null || !(expenseDTO.getCurrencyId() instanceof Integer) || !(expenseDTO.getUserId() instanceof Long) ||
                !(expenseDTO.getPaymentMethodId() instanceof Integer) || !(expenseDTO.getCategoryId() instanceof Long) ||
                (!(expenseDTO.getAmount() instanceof BigDecimal) && validateBigDecimal(expenseDTO.getAmount(), 10, 2)) ||
                (!(expenseDTO.getExpenseDescription() instanceof String) && (expenseDTO.getExpenseDescription().isEmpty() || expenseDTO.getExpenseDescription().length() > 1000))) {

                    throw new ValidationException("Non null field value must not be null or empty and characters must not exceed limit.", HttpStatus.BAD_REQUEST);
            }
            if(paymentMethodService.getPaymentMethodById(expenseDTO.getPaymentMethodId()) != null &&
                currencyService.getCurrencyById(expenseDTO.getCurrencyId()) != null &&
                categoryService.getCategoryById(expenseDTO.getCategoryId()) != null &&
                userService.getUserById(expenseDTO.getUserId()) != null) {
                    // exception will be thrown in their respective services if wrong reference is given
            }
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

    public void deleteExpense(long expenseId, Long userId) {

        Expense existingExpense = null;
        try {
            if(this.getExpenseById(expenseId).getApprovalStatus() != ApprovalStatus.NOT_APPROVED) {
                throw new ValidationException("Your expense has already been either approved or rejected", HttpStatus.FORBIDDEN);
            }
            existingExpense = expenseRepository.findExpenseById(expenseId);
            if(this.getExpenseById(expenseId) != null && existingExpense.getUser().getUserId().equals(userId)) {
                expenseRepository.deleteById(expenseId);
            }
        }
        catch (ValidationException e) {
            throw e;
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
        expense.setExpenseDate(ZonedDateTime.now(ZoneId.of("UTC")));
        expense.setApprovalStatus(ApprovalStatus.NOT_APPROVED);
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
        List<Budget> budgets = budgetRepository.findBudgetsByCategory(categoryId);

        BigDecimal lastOneWeekExpenses = updatedBudget.add(this.getSumOfLastSevenDaysExpenses(categoryId)).subtract(currentBudget);
        BigDecimal lastOneMonthExpenses = updatedBudget.add(this.getSumOfLastOneMonthExpenses(categoryId)).subtract(currentBudget);
        BigDecimal lastOneYearExpenses = updatedBudget.add(this.getSumOfLastOneYearExpenses(categoryId)).subtract(currentBudget);

        for (Budget budget : budgets) {
                
            if(String.valueOf(budget.getBudgetType()).equalsIgnoreCase("ANNUAL") && budget.getBudgetAmount().compareTo(lastOneYearExpenses) < 0) {
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
    public List<ExpenseDTO> getByAmountSpentHigherToLower(Long organizationId) {
        
        List<Expense> expenseList = null;
        try {
            expenseList = expenseRepository.findByAmount(organizationId);
            
            return this.getExpenses(expenseList, "No expenses found");
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while getting expenses");
        }
    }

    public List<ExpenseDTO> getByCategoryName(Long organizationId, String categoryName) {

        List<Expense> expenseList = null;

        try {
            expenseList = expenseRepository.findByCategoryName(organizationId, categoryName);

            String notFoundMessage = "No expense found for the category name " + categoryName;
            return this.getExpenses(expenseList, notFoundMessage);
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while retrieving expenses for category name " + categoryName);
        }
    }

    public List<ExpenseDTO> getByCurrencyCode(Long organizationId, String currencyCode) {

        List<Expense> expenseList = null;
        try {
            expenseList = expenseRepository.findByCurrencyCode(organizationId, currencyCode);

            String notFoundMessage = "No expense found for the currency code " + currencyCode;
            return this.getExpenses(expenseList, notFoundMessage);
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while retrieving expenses for currencyCode " + currencyCode);
        }
    }

    public List<ExpenseDTO> getExpensesByUserId(Long userId) {

        List<Expense> expenseList = null;
        try {
            expenseList = expenseRepository.findExpensesByUserId(userId);
            
            String notFoundMessage = "No expense found";
            return this.getExpenses(expenseList, notFoundMessage);
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while retrieving recent expenses");
        }
    }

    public List<ExpenseDTO> getByPaymentMethodName(Long organizationId, String paymentMethodName) {

        List<Expense> expenseList = null;
        try {
            expenseList = expenseRepository.findByPaymentMethodName(organizationId, paymentMethodName);

            String notFoundMessage = "No expense found for the paymentMethod Name " + paymentMethodName;
            return this.getExpenses(expenseList, notFoundMessage);
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while retrieving expenses for paymentMethod Name " + paymentMethodName);
        }
    }




    //approver's access
    public List<ExpenseDTO> getRecentExpenses(Long approverId, Long organizationId) {

        List<Expense> expenseList = null;
        try {
            if(this.isApprover(approverId)) {
                expenseList = expenseRepository.findByDate(organizationId);
            }
            
            String notFoundMessage = "No expense found";
            return this.getExpenses(expenseList, notFoundMessage);
        }
        catch(ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while retrieving recent expenses");
        }
    }

    public List<ExpenseDTO> getExpensesByStatus(Long approverId, Long organizationId, ApprovalStatus approvalStatus) {
        
        List<Expense> expenseList = new ArrayList<>();
        List<ExpenseDTO> expenseDTOList = new ArrayList<>();
        try {
            if(this.isApprover(approverId)) {
                expenseList = expenseRepository.findExpensesByStatus(organizationId, approvalStatus);
                expenseDTOList = this.getExpenses(expenseList, "No expenses found with the approval status " + approvalStatus);
            }
            return expenseDTOList;
        }
        catch(ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while retrieving expense by approval status " + approvalStatus);
        }
    }
    
    public void updateApprovalStatus(Long approverId, Long expenseId, Boolean statusMessage) {
        
        Expense existingExpense = null;
        List<ApprovalsDTO> approvalList = new ArrayList<>();
        try {
            if(this.isApprover(approverId) && this.getExpenseById(expenseId) != null) {
                ApprovalStatus approvalStatus;
                
                existingExpense = expenseRepository.findExpenseById(expenseId);
                approvalList = approvalsService.getByExpenseId(expenseId);

                if(Boolean.TRUE.equals(statusMessage)) {
                    if(approvalList.isEmpty()) {
                        approvalStatus = ApprovalStatus.PARTIAL;
                    }
                    else if(approvalList.size() == 1) {
                        approvalStatus = ApprovalStatus.APPROVED;
                    }
                    else {
                        throw new ValidationException("Already approved", HttpStatus.BAD_REQUEST);
                    }
                }
                else {
                    approvalStatus = ApprovalStatus.REJECTED;
                }
                
                existingExpense.setApprovalStatus(approvalStatus);
                approvalsService.addApproval(approverId, expenseId);        //adds a new record in approvals table
            }
            expenseRepository.saveAndFlush(existingExpense);
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while updating expense.");
        }
    }
    
    public boolean isApprover(Long userId) {
        User approver = null;
        try {
            approver = userRepository.findUserById(userId);
            if(approver != null) {
                Set<Role> roles = approver.getRoles();

                for (Role role : roles) {
                    if(role.getRoleName().equals("APPROVER"))
                        return true;
                }
                return false;
            }
            else {
                throw new ValidationException("Access denied", HttpStatus.FORBIDDEN);
            }
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("an unexpected error occured while verifying approver");
        }
    }
}
