package com.zoho.controller;
import com.zoho.entity.SignUpUser;
import com.zoho.payload.LoginDto;
import com.zoho.payload.TokenDto;
import com.zoho.repository.SignUpUserRepository;
import com.zoho.service.SignUpUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v/user")
public class SignUpUserController {

    private SignUpUserRepository signUpUserRepository;

    private SignUpUserService signUpUserService;

    public SignUpUserController(SignUpUserRepository signUpUserRepository, SignUpUserService signUpUserService) {
        this.signUpUserRepository = signUpUserRepository;
        this.signUpUserService = signUpUserService;
    }
//   http://localhost:8080/api/v/user/signup


    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@RequestBody SignUpUser signUpUser) {

        Optional<SignUpUser> opUsername = signUpUserRepository.findByUsername(signUpUser.getUsername());
        if (opUsername.isPresent()) {
            return new ResponseEntity<>("User name alreday exist", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Optional<SignUpUser> opEmail = signUpUserRepository.findByEmail(signUpUser.getEmail());
        if (opEmail.isPresent()) {
            return new ResponseEntity<>("Email already exist", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String enPass = BCrypt.hashpw(signUpUser.getPassword(), BCrypt.gensalt(5));
        signUpUser.setPassword(enPass);
        SignUpUser saved = signUpUserRepository.save(signUpUser);

        return new ResponseEntity<>(saved, HttpStatus.CREATED);

    }
//http://localhost:8080/api/v/user/message


    @GetMapping("/message")
    public String getMessage() {

        return "Hello";
    }


   // http://localhost:8080/api/v/user/login

    @PostMapping("/login")
    public ResponseEntity<?> verifyLogin(@RequestBody LoginDto dto) {

        String token = signUpUserService.verifyLogin(dto);
        if (token!=null) {

            TokenDto tokenDto = new TokenDto();
            tokenDto.setToken(token);
            tokenDto.setType("JWT");

            return new ResponseEntity<>(tokenDto, HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid username/password!!", HttpStatus.FORBIDDEN);
    }


}
