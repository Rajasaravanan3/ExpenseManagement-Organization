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

import com.example.demo.ExpenseManagement.Entity.Role;
import com.example.demo.ExpenseManagement.Service.RoleService;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;
    
    @GetMapping("/{roleId}")
    public ResponseEntity<Role> getRoleById(@PathVariable("roleId") Long roleId) {
        return new ResponseEntity<>(roleService.getRoleById(roleId), HttpStatus.OK);
    }

    //admin's access
    @PostMapping
    public ResponseEntity<Void> addRole(@RequestBody Role role) {

        roleService.addRole(role);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //admin's access
    @PutMapping
    public ResponseEntity<Void> updateApprover(@RequestBody Role role) {

        roleService.updateRole(role);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
