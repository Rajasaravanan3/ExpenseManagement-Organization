package com.example.demo.ExpenseManagement.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
        return new ResponseEntity(notificationMessage, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<String> updateExpense(@RequestBody ExpenseDTO expenseDTO) {

        String notificationMessage = expenseService.updateExpense(expenseDTO);
        return new ResponseEntity(notificationMessage, HttpStatus.OK);
    }


    //filters
    @GetMapping("/categories/{categoryName}")
    public ResponseEntity<List<ExpenseDTO>> getExpensesByCategoryName(@PathVariable("categoryName") String categoryName) {
        
        return new ResponseEntity<>(expenseService.getByCategoryName(categoryName), HttpStatus.OK);
    }

    //filter expenses by following

    // get expense list by amount higher to lower

    // get expense list by budget amount

    // get expenses by category name

    //get expenses by currency code

    //get expenses by payment method name



    // if user role is admin - get expenses by other's userId

}
