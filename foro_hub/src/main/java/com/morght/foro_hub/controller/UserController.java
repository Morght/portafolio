package com.morght.foro_hub.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.morght.foro_hub.payload.RegisterRequest;
import com.morght.foro_hub.payload.UserResponse;
import com.morght.foro_hub.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody @Valid RegisterRequest body,
            UriComponentsBuilder uriBilder) {
        UserResponse userResponse = userService.createUser(body);
        URI url = uriBilder.path("api/users/{id}").buildAndExpand(userResponse.id()).toUri();
        return ResponseEntity.created(url).body(userResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {

        return ResponseEntity.ok(userService.getById(id));
    }
}
