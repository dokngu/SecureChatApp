package com.example.demo.Controller;

import com.example.demo.Model.MyAppUser;
import com.example.demo.Model.MyAppUserRepository;
import com.example.demo.Model.PasswordChangeForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private MyAppUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/changepw")
    public String changePassword(@Validated PasswordChangeForm passwordChangeForm, Model model){

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<MyAppUser> user = userRepository.findByUsername(username);
        if(user.isEmpty()) {
            model.addAttribute("error", "User not found.");
            return "changepw";  //stay on change password page with error
        }
        MyAppUser currentUser = user.get();

        if (!passwordEncoder.matches(passwordChangeForm.getOldPassword(), currentUser.getPassword())) {
            model.addAttribute("error", "Old password is incorrect.");
            return "changepw";  //stay on the change password page with error
        }

        //at this point everything is good, update pw
        currentUser.setPassword(passwordEncoder.encode(passwordChangeForm.getNewPassword()));
        userRepository.save(currentUser);  // Save the updated user to the database

        model.addAttribute("success", "Password successfully changed.");
        return "redirect:/chat";
    }

}
