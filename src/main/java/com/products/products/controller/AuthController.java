package com.products.products.controller;

import com.products.products.dto.ReqRes;
import com.products.products.model.User;
import com.products.products.service.AuthenticationService;
import com.products.products.service.JWTUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JWTUtils jwtUtils;
    private final AuthenticationService authenticationService;

    public AuthController(JWTUtils jwtUtils, AuthenticationService authenticationService) {
        this.jwtUtils = jwtUtils;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ReqRes> register(@RequestBody ReqRes signUpRequest){
        return new ResponseEntity<>(authenticationService.signUp(signUpRequest), HttpStatus.OK);
    }
    @PostMapping("/signin")
    public ResponseEntity<ReqRes> authenticate(@RequestBody ReqRes signInRequest){
        User authenticatedUser = authenticationService.signIn(signInRequest).getUser();
        var jwtToken =jwtUtils.generateToken(authenticatedUser);

        return new ResponseEntity<>(authenticationService.signIn(signInRequest), HttpStatus.OK);
    }

}
