package com.i2i.zapcab.controller;

import com.i2i.zapcab.dto.AuthenticationRequestDto;
import com.i2i.zapcab.dto.AuthenticationResponse;
import com.i2i.zapcab.dto.RegisterUserRequestDto;
import com.i2i.zapcab.dto.RegisterDriverDto;
import com.i2i.zapcab.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/register/customer")
    public ResponseEntity<AuthenticationResponse> registerCustomer(@RequestBody RegisterUserRequestDto registerRequestDto) {
        System.out.println("register customer");
        return ResponseEntity.ok(authenticationService.customerRegister(registerRequestDto));
    }
    @PostMapping("/register/admin")
    public ResponseEntity<AuthenticationResponse> registerAdmin(@RequestBody RegisterUserRequestDto registerRequestDto) {
        System.out.println("register Admin");
        return ResponseEntity.ok(authenticationService.adminRegister(registerRequestDto));
    }

    @PostMapping("/register/driver")
    public ResponseEntity<AuthenticationResponse> registerDriver(@RequestBody RegisterDriverDto registerDriverDto){
        return ResponseEntity.ok(authenticationService.driverRequest(registerDriverDto));
    }

    @PostMapping
    public ResponseEntity<AuthenticationResponse> authentication(@RequestBody AuthenticationRequestDto authenticationRequsetDto) {
        System.out.println("authentication processing");
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticationRequsetDto);
        if(authenticationResponse == null){
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(authenticationResponse);
    }

}
