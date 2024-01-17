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

import com.example.demo.ExpenseManagement.DTO.OrganizationDTO;
import com.example.demo.ExpenseManagement.Service.OrganizationService;

@RestController
@RequestMapping("/organizations")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;
    
    @GetMapping("/{organizationId}")
    public ResponseEntity<OrganizationDTO> getOrganizationById(@PathVariable Long organizationId) {
        
        return new ResponseEntity<>(organizationService.getOrganizationById(organizationId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> addOrganization(@RequestBody OrganizationDTO organizationDTO) {

        organizationService.addOrganization(organizationDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Void> updateOrganization(@RequestBody OrganizationDTO organizationDTO) {
        
        organizationService.updateOrganization(organizationDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
