package com.morght.foro_hub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.morght.foro_hub.models.User;
import com.morght.foro_hub.payload.RegisterRequest;
import com.morght.foro_hub.payload.UserResponse;
import com.morght.foro_hub.payload.auth.AuthenticationRequest;
import com.morght.foro_hub.repository.UserRepository;

@Service
public class UserService {
        @Autowired
        UserRepository userRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private AuthenticationManager authenticationManager;

        public UserResponse createUser(RegisterRequest data) {

                return new UserResponse(userRepository.save(
                                new User(data.name(),
                                                data.email(),
                                                passwordEncoder.encode(data.password())

                                )));
        }

        public UserResponse getById(Long id) {
                // TODO valid existance
                return new UserResponse(userRepository.getReferenceById(id));
        }

        public User getUserByEmail(String email) {
                return (User) userRepository.findByEmail(email);
        }

        public Authentication authenticate(AuthenticationRequest request) {
                return authenticationManager
                                .authenticate(new UsernamePasswordAuthenticationToken(
                                                request.email(),
                                                request.password()));

        }
}
