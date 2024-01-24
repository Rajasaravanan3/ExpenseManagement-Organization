package com.example.demo.ExpenseManagement.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.ExpenseManagement.DTO.UserDTO;
import com.example.demo.ExpenseManagement.Service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("userId") Long userId) {
        
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<UserDTO> getUserByEmail(@RequestParam("email") String email) {
        return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> addUser(@RequestBody UserDTO userDTO) {

        userService.addUser(userDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Void> updateUser(@RequestBody UserDTO userDTO) {

        userService.updateUser(userDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //get users working for the organization
    //admin's access
    @GetMapping("/{adminId}/all")
    public ResponseEntity<List<UserDTO>> getAllUsers(@PathVariable("adminId") Long adminId, @RequestParam("organizationId") Long organizationId) {
        
        List<UserDTO> userDTOList = userService.getAllUsers(organizationId, adminId);
        if(userDTOList != null) {
            return new ResponseEntity<>(userDTOList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //get users by role name
    //admin's access
    @GetMapping("/{adminId}/role")
    public ResponseEntity<List<UserDTO>> getUsersByRoleName(@PathVariable("adminId") Long adminId, @RequestParam("organizationId") Long organizationId, @RequestParam("roleName") String roleName) {
        
        List<UserDTO> userDTOList = userService.getUsersByRoleName(organizationId, adminId, roleName);
        if(userDTOList != null) {
            return new ResponseEntity<>(userDTOList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
