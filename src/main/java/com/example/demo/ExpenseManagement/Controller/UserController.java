package com.example.demo.ExpenseManagement.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.ExpenseManagement.DTO.RoleUpdate;
import com.example.demo.ExpenseManagement.DTO.UserDTO;
import com.example.demo.ExpenseManagement.Service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<UserDTO> getUserByUsername(@RequestParam("username") String username) {

        return new ResponseEntity<>(userService.getUserByUsername(username), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Void> updateUser(@RequestBody UserDTO userDTO) {

        userService.updateUser(userDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // get users working for an organization
    // admin's access
    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers(@RequestParam("organizationId") Long organizationId) {

        List<UserDTO> userDTOList = userService.getAllUsers(organizationId);
        if (userDTOList != null) {
            return new ResponseEntity<>(userDTOList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // admin's access
    @PutMapping("/add-role")
    public ResponseEntity<Void> addRoleToUser(@RequestBody RoleUpdate roleUpdate) {

        userService.addRoleToUser(roleUpdate);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // admin's access
    @PutMapping("/remove-role")
    public ResponseEntity<Void> removeRoleFromUser(@RequestBody RoleUpdate roleUpdate) {

        userService.removeRoleFromUser(roleUpdate);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
