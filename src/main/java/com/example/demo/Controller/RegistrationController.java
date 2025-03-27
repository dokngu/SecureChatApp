package com.example.demo.Controller;

import com.example.demo.Exception.UsernameTakenException;
import com.example.demo.Model.MyAppUser;
import com.example.demo.Model.MyAppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import org.owasp.encoder.Encode;

@RestController
public class RegistrationController {

    @Autowired
    private MyAppUserRepository myAppUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping(value = "/req/signup", consumes = "application/json")
    public MyAppUser createUser(@RequestBody MyAppUser user){

        Optional<MyAppUser> existingUser = myAppUserRepository.findByUsername(user.getUsername());
        if(existingUser.isPresent()){
            throw new UsernameTakenException("Username already exists");
        }

        //at this point we know username is not a dupe
        //SECURITY: encode usernames through OWASP Encoder before adding to database
        user.setUsername(Encode.forHtml(user.getUsername()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return myAppUserRepository.save(user);
    }
}
