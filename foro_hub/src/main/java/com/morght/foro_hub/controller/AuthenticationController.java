package com.morght.foro_hub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.morght.foro_hub.infra.security.TokenService;
import com.morght.foro_hub.models.User;
import com.morght.foro_hub.payload.auth.AuthenticationRequest;
import com.morght.foro_hub.payload.auth.AuthenticationResponse;
import com.morght.foro_hub.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

    @Autowired
    UserService userService;

    @Autowired
    TokenService tokenService;

    @PostMapping
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest authReq) {

        Authentication auth = userService.authenticate(authReq);
        return ResponseEntity.ok(
                new AuthenticationResponse(
                        tokenService.createToken(
                                (User) auth.getPrincipal()

                        )));
    }

}