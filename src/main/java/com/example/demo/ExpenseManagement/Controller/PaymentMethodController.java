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

import com.example.demo.ExpenseManagement.Entity.PaymentMethod;
import com.example.demo.ExpenseManagement.Service.PaymentMethodService;

@RestController
@RequestMapping("/paymentMethods")
public class PaymentMethodController {
    
    @Autowired
    private PaymentMethodService paymentMethodService;

    @GetMapping("/{paymentMethodId}")
    public ResponseEntity<PaymentMethod> getPaymentMethod(@PathVariable("paymentMethodId") Integer paymentMethodId) {
        
        PaymentMethod paymentMethod = paymentMethodService.getPaymentMethodById(paymentMethodId);
        return new ResponseEntity<>(paymentMethod, HttpStatus.OK);
    }

    //admin's access
    @PostMapping
    public ResponseEntity<Void> addPaymentMethod(@RequestBody PaymentMethod paymentMethod) {

        paymentMethodService.addPaymentMethod(paymentMethod);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //admin's access
    @PutMapping
    public ResponseEntity<Void> updatePaymentMethod(@RequestBody PaymentMethod paymentMethod) {

        paymentMethodService.updatePaymentMethod(paymentMethod);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
