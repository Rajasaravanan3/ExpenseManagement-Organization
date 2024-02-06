package com.example.demo.ExpenseManagement.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.ExpenseManagement.DTO.JwtAuthenticationResponse;
import com.example.demo.ExpenseManagement.DTO.RefreshTokenRequest;
import com.example.demo.ExpenseManagement.DTO.SignInRequest;
import com.example.demo.ExpenseManagement.DTO.SignUpRequest;
import com.example.demo.ExpenseManagement.Entity.User;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody SignUpRequest signUpRequest) {

        return new ResponseEntity<>(authenticationService.signUp(signUpRequest), HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@RequestBody SignInRequest signInRequest) throws Exception {
        return new ResponseEntity<>(authenticationService.signIn(signInRequest), HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) throws Exception {
        return new ResponseEntity<>(authenticationService.refreshToken(refreshTokenRequest), HttpStatus.OK);
    }
}
