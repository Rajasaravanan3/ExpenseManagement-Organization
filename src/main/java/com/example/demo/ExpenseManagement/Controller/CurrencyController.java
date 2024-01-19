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

import com.example.demo.ExpenseManagement.Entity.Currency;
import com.example.demo.ExpenseManagement.Service.CurrencyService;

@RestController
@RequestMapping("/currencies")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;
    
    @GetMapping("/{currencyId}")
    public ResponseEntity<Currency> getCurrencyById(@PathVariable("currencyId") Integer currencyId) {
        return new ResponseEntity<>(currencyService.getCurrencyById(currencyId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> addCurrency(@RequestBody Currency currency) {

        currencyService.addCurrency(currency);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Void> updateCurrency(@RequestBody Currency currency) {

        currencyService.updateCurrency(currency);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
