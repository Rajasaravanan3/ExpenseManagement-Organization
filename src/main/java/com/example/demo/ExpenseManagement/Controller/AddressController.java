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

import com.example.demo.ExpenseManagement.Entity.Address;
import com.example.demo.ExpenseManagement.Service.AddressService;

@RestController
@RequestMapping("/addresses")
public class AddressController {
    
    @Autowired
    private AddressService addressService;

    @GetMapping("/{addressId}")
    public ResponseEntity<Address> getAddressById(@PathVariable("addressId") Long addressId) {

        return new ResponseEntity<>(addressService.getAddressById(addressId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> addAddress(@RequestBody Address address) {

        addressService.addAddress(address);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Void> updateAddress(@RequestBody Address address) {
        
        addressService.updateAddress(address);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
