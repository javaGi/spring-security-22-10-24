package com.zoho.service;


import com.zoho.entity.SignUpUser;
import com.zoho.payload.LoginDto;
import com.zoho.repository.SignUpUserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SignUpUserService {

    private SignUpUserRepository signUpUserRepository;
    private JWTService jwtService;

    public SignUpUserService(SignUpUserRepository signUpUserRepository, JWTService jwtService) {
        this.signUpUserRepository = signUpUserRepository;
        this.jwtService = jwtService;
    }


    public String verifyLogin(LoginDto dto) {
        Optional<SignUpUser> opUser = signUpUserRepository.findByUsername(dto.getUsername());
        if (opUser.isPresent()) {
            SignUpUser signUpUser = opUser.get();
            if (BCrypt.checkpw(dto.getPassword(), signUpUser.getPassword())) {
                //generate token
                String token = jwtService.generateToken(signUpUser.getUsername());
                return token;

            }
            } else {

                return null;
            }

        return null;

    }
}