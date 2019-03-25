package com.sweater.controller;

import com.sweater.domain.User;
import com.sweater.service.UserSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class RegistrationController {
    @Autowired
    private UserSevice userSevice;
    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@RequestParam("password2") String passwordConfirm, @Valid User user, BindingResult bindingResult, Model model){

        boolean isPasswordDifferent = user.getPassword() != null && !user.getPassword().equals(passwordConfirm);
        if(isPasswordDifferent){
            model.addAttribute("passwordError", "Passwords are different!");
        }

        boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirm);
        if(isConfirmEmpty){
            model.addAttribute("password2Error","Password confirmation cannot be empty");
        }

        if(user.getPassword()!=null && !user.getPassword().equals(passwordConfirm)){
            model.addAttribute("passwordError", "Password are different!!!");
        }
        if(isConfirmEmpty || bindingResult.hasErrors()||isPasswordDifferent){
            Map<String, String> errors = ControllerUtiles.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "registration";
        }
        if(!userSevice.addUser(user)){
            model.addAttribute("usernameError", "User exists!");
            return "registration";
        }


        return "redirect:/login";

    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code){
        boolean isActivated=userSevice.activateUser(code);
        if(isActivated){
            model.addAttribute("messageType","success");
            model.addAttribute("message","User successfully activated");
        }else {
            model.addAttribute("message","danger");
            model.addAttribute("message","Activation code is not found");
        }

        return "login";
    }


}
