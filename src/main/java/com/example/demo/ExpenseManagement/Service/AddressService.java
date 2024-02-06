package com.example.demo.ExpenseManagement.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.ExpenseManagement.Entity.Address;
import com.example.demo.ExpenseManagement.ExceptionController.ApplicationException;
import com.example.demo.ExpenseManagement.ExceptionController.ValidationException;
import com.example.demo.ExpenseManagement.Repository.AddressRepository;
import com.example.demo.ExpenseManagement.Security.JWTService;

@Service
public class AddressService {
    
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private JWTService jwtService;

    public Address getAddressById(Long addressId) {

        Address address = null;
        try {
            address = addressRepository.findAddressById(addressId);
            if(address == null) {
                throw new ValidationException("No record found for the addressId "+ addressId, HttpStatus.NOT_FOUND);
            }
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected Error occurred while retrieving");
        }
        return address;
    }

    public void addAddress(Address address) {

        try {
            if(address == null || (address.getCity() instanceof String && (address.getCity().isEmpty() || address.getCity().length() > 25)) ||
            (address.getStreet() instanceof String && (address.getStreet().length() > 30)) ||
            (address.getState() instanceof String && (address.getState().isEmpty() || address.getState().length() > 30)) ||
            (address.getCountry() instanceof String && (address.getCountry().isEmpty() || address.getCountry().length() > 30)) ||
            (address.getZipCode() instanceof String && (address.getZipCode().isEmpty() || address.getZipCode().length() > 10))) {

                throw new ValidationException("Non null field value must not be null or empty and characters must not exceed limit", HttpStatus.BAD_REQUEST);
            }
            addressRepository.saveAndFlush(address);
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while saving");
        }
    }
    
    public void updateAddress(Address updatedAddress) {

        Address existingAddress = null;
        try {
            existingAddress = this.getAddressById(updatedAddress.getAddressId());

            if(updatedAddress.getStreet() instanceof String) {
                
                if(updatedAddress.getStreet().length() >30)
                    throw new ValidationException("Street name must not exceed 30 characters", HttpStatus.BAD_REQUEST);
                existingAddress.setStreet(updatedAddress.getStreet());
            }
            if(updatedAddress.getCity() instanceof String && !(updatedAddress.getCity()).isEmpty()) {
                
                if(updatedAddress.getCity().length() > 25)
                    throw new ValidationException("City name must not exceed 25 characters", HttpStatus.BAD_REQUEST);
                existingAddress.setCity(updatedAddress.getCity());
            }
            if(updatedAddress.getState() instanceof String && !(updatedAddress.getState()).isEmpty()) {

                if(updatedAddress.getState().length() > 30)
                    throw new ValidationException("State name must not exceed 30 characters", HttpStatus.BAD_REQUEST);
                existingAddress.setState(updatedAddress.getState());
            }
            if(updatedAddress.getCountry() instanceof String && !(updatedAddress.getCountry()).isEmpty()) {

                if(updatedAddress.getCountry().length() > 30)
                    throw new ValidationException("Country name must not exceed 30 characters", HttpStatus.BAD_REQUEST);
                existingAddress.setCountry(updatedAddress.getCountry());
            }
            if(updatedAddress.getZipCode() instanceof String && !(updatedAddress.getZipCode()).isEmpty()) {

                if(updatedAddress.getZipCode().length() > 10)
                    throw new ValidationException("Provide a valid Zip code", HttpStatus.BAD_REQUEST);
                existingAddress.setZipCode(updatedAddress.getZipCode());
            }
            addressRepository.saveAndFlush(existingAddress);
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while updating");
        }

    }
}
