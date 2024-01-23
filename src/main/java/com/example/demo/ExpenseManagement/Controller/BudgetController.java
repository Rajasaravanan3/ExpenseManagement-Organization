package com.example.demo.ExpenseManagement.Controller;

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

import com.example.demo.ExpenseManagement.DTO.BudgetDTO;
import com.example.demo.ExpenseManagement.Service.BudgetService;

@RestController
@RequestMapping("/budgets")
public class BudgetController {
    
    @Autowired
    private BudgetService budgetService;

    @GetMapping("/{budgetId}")
    public ResponseEntity<BudgetDTO> getBudgetById(@PathVariable("budgetId") Long budgetId) {

        BudgetDTO budgetDTO = budgetService.getBudgetById(budgetId);
        return new ResponseEntity<>(budgetDTO, HttpStatus.OK);
    }

    // change to only admin can add budget
    @PostMapping
    public ResponseEntity<Void> addBudget(@RequestBody BudgetDTO budgetDTO) {

        budgetService.addBudget(budgetDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
