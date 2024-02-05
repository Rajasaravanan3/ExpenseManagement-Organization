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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.ExpenseManagement.DTO.UserDTO;
import com.example.demo.ExpenseManagement.Entity.Role;
import com.example.demo.ExpenseManagement.Service.RoleService;
import com.example.demo.ExpenseManagement.Service.UserService;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;
    
    @GetMapping("/{roleId}")
    public ResponseEntity<Role> getRoleById(@PathVariable("roleId") Long roleId) {
        return new ResponseEntity<>(roleService.getRoleById(roleId), HttpStatus.OK);
    }

    // get users by role name
    // admin's access
    @GetMapping("/role")
    public ResponseEntity<List<UserDTO>> getUsersByRoleName(@RequestParam("organizationId") Long organizationId, @RequestParam("roleName") String roleName) {
        
        List<UserDTO> userDTOList = roleService.getUsersByRoleName(organizationId, roleName);
        if(userDTOList != null) {
            return new ResponseEntity<>(userDTOList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
