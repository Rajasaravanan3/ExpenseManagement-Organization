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

    @PutMapping("/{expenseId}/{approvalStatus}")
    public ResponseEntity<String> updateExpense(@RequestParam("expenseId") Long expenseId,@RequestParam("approvalStatus") String approvalStatus) {

        String notificationMessage = expenseService.updateExpense(expenseId, approvalStatus);
        return new ResponseEntity<>(notificationMessage, HttpStatus.OK);
    }

    @DeleteMapping("/{expenseId}/{userId}")
    public ResponseEntity<Void> deleteExpense(@RequestParam("expenseId") Long expenseId,@RequestParam("userId") Long userId) {

        //write service method
        return new ResponseEntity<>(HttpStatus.OK);
    }




    //filters

    // get expenses by category name
    @GetMapping("/filters/{categoryName}")
    public ResponseEntity<List<ExpenseDTO>> getExpensesByCategoryName(@RequestParam("categoryName") String categoryName) {
        
        List<ExpenseDTO> expenseDTOList = expenseService.getByCategoryName(categoryName);
        if(expenseDTOList != null) {
            return new ResponseEntity<>(expenseService.getByCategoryName(categoryName), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // get expense list by amount higher to lower
    @GetMapping("/filters/amount-spent")
    public ResponseEntity<List<ExpenseDTO>> getByAmountSpentHigherToLower() {

        List<ExpenseDTO> expenseDTOList = expenseService.getByAmountSpentHigherToLower();
        if(expenseDTOList != null) {
            return new ResponseEntity<>(expenseDTOList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //get expenses by currency code
    @GetMapping("/filters/{currencyCode}")
    public ResponseEntity<List<ExpenseDTO>> getExpensesByCurrencyCode(@RequestParam("currencyCode") String currencyCode) {
        
        List<ExpenseDTO> expenseDTOList = expenseService.getByCurrencyCode(currencyCode);
        if(expenseDTOList != null) {
            return new ResponseEntity<>(expenseService.getByCurrencyCode(currencyCode), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //get expenses by payment method name
    @GetMapping("/filters/{paymentMethodName}")
    public ResponseEntity<List<ExpenseDTO>> getExpensesByPaymentMethodName(@RequestParam("paymentMethodName") String paymentMethodName) {
        
        List<ExpenseDTO> expenseDTOList = expenseService.getByPaymentMethodName(paymentMethodName);
        if(expenseDTOList != null) {
            return new ResponseEntity<>(expenseDTOList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



    
    //admin's access

    //get expenses by other's userId
    @GetMapping("/admin/{adminId}{userId}")
    public ResponseEntity<List<ExpenseDTO>> getExpensesByUserId(@RequestParam("adminId") Long adminId, @RequestParam("userId") Long userId) {
        
        List<ExpenseDTO> expenseDTOList = expenseService.getExpensesByUserId(adminId, userId);
        if(expenseDTOList != null) {
            return new ResponseEntity<>(expenseDTOList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    
    // @GetMapping("/recent-expenses")
    // public ResponseEntity<List<ExpenseDto>> getByRecentExpenses() {

    //     List<ExpenseDto> expenseDtoList = expenseService.getByRecentExpenses();
    //     if(expenseDtoList != null) {
    //         return new ResponseEntity<>(expenseDtoList, HttpStatus.OK);
    //     }
    //     return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    // }
}
