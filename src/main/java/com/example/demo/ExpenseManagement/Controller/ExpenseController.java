package com.example.demo.ExpenseManagement.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.ExpenseManagement.DTO.ExpenseDTO;
import com.example.demo.ExpenseManagement.Service.ApprovalsService;
import com.example.demo.ExpenseManagement.Service.ExpenseService;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;
    
    @GetMapping("/{expenseId}")
    public ResponseEntity<ExpenseDTO> getExpenseById(@PathVariable("expenseId") Long expenseId) {

        ExpenseDTO expenseDTO = expenseService.getExpenseById(expenseId);
        return new ResponseEntity<>(expenseDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addExpense(@RequestBody ExpenseDTO expenseDTO) {

        String notificationMessage = expenseService.addExpense(expenseDTO);
        return new ResponseEntity<>(notificationMessage, HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteExpense(@RequestParam("expenseId") Long expenseId, @RequestParam("userId") Long userId) {

        expenseService.deleteExpense(expenseId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }




    //expense filters

    // get expenses by category name
    @GetMapping("/filters/category-name")
    public ResponseEntity<List<ExpenseDTO>> getExpensesByCategoryName(@RequestParam("organizationId") Long organizationId, @RequestParam("categoryName") String categoryName) {
        
        List<ExpenseDTO> expenseDTOList = expenseService.getByCategoryName(organizationId, categoryName);
        if(expenseDTOList != null) {
            return new ResponseEntity<>(expenseService.getByCategoryName(organizationId, categoryName), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // get expense list by amount higher to lower
    @GetMapping("/filters/amount-spent")
    public ResponseEntity<List<ExpenseDTO>> getByAmountSpentHigherToLower(@RequestParam("organizationId") Long organizationId) {

        List<ExpenseDTO> expenseDTOList = expenseService.getByAmountSpentHigherToLower(organizationId);
        if(expenseDTOList != null) {
            return new ResponseEntity<>(expenseDTOList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //get expenses by currency code
    @GetMapping("/filters/currency-Code")
    public ResponseEntity<List<ExpenseDTO>> getExpensesByCurrencyCode(@RequestParam("organizationId") Long organizationId, @RequestParam("currencyCode") String currencyCode) {
        
        List<ExpenseDTO> expenseDTOList = expenseService.getByCurrencyCode(organizationId, currencyCode);
        if(expenseDTOList != null) {
            return new ResponseEntity<>(expenseDTOList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //get expenses by userId
    @GetMapping("/filters/user/{userId}")
    public ResponseEntity<List<ExpenseDTO>> getExpensesByUserId(@PathVariable("userId") Long userId) {
        
        List<ExpenseDTO> expenseDTOList = expenseService.getExpensesByUserId(userId);
        if(expenseDTOList != null) {
            return new ResponseEntity<>(expenseDTOList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //get expenses by payment method name
    @GetMapping("/filters/payment-Method-name")
    public ResponseEntity<List<ExpenseDTO>> getExpensesByPaymentMethodName(@RequestParam("organizationId") Long organizationId, @RequestParam("paymentMethodName") String paymentMethodName) {
        
        List<ExpenseDTO> expenseDTOList = expenseService.getByPaymentMethodName(organizationId, paymentMethodName);
        if(expenseDTOList != null) {
            return new ResponseEntity<>(expenseDTOList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



    
    //admin's access

    //get users working for the organization
    //get all approvals done by an user(approver)
    // get users by roleName
    //update category
    //update budget
    //update approver
    



    //approver's accesss
    
    //get recent expenses for the organization
    @GetMapping("/approver/{approverId}/recent-expenses")
    public ResponseEntity<List<ExpenseDTO>> getByRecentExpenses(@PathVariable("approverId") Long approverId, @RequestParam("organizationId") Long organizationId) {

        List<ExpenseDTO> expenseDtoList = expenseService.getRecentExpenses(approverId, organizationId);
        if(expenseDtoList != null) {
            return new ResponseEntity<>(expenseDtoList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //get expenses by approval status
    @GetMapping("/approver/{approverId}/approval-status")
    public ResponseEntity<List<ExpenseDTO>> getExpensesByStatus(@PathVariable("approverId") Long approverId, @RequestParam("organizationId") Long organizationId, @RequestParam("approvalStatus") String approvalStatus) {

        List<ExpenseDTO> expenseDtoList = expenseService.getExpensesByStatus(approverId, organizationId, approvalStatus);
        if(expenseDtoList != null) {
            return new ResponseEntity<>(expenseDtoList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //update approval status -> after approving or rejecting the expense
    @PutMapping("/approver/{approverId}/approval-status")
    public ResponseEntity<Void> updateApprovalStatus(@PathVariable("approverId") Long approverId, @RequestParam("expenseId") Long expenseId, @RequestParam("approvalStatus") String approvalStatus) {

        expenseService.updateApprovalStatus(approverId, expenseId, approvalStatus);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
