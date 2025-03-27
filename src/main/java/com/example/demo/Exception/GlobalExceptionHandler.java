package com.example.demo.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameTakenException.class)
    public String usernameTakenExceptionHandler(UsernameTakenException e, Model model) {
        System.out.println("ERROR:" + e.getMessage());
        model.addAttribute("error", e.getMessage());
        return "signup";
    }


}
