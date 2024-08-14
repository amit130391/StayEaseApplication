package com.crio.Stayease.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crio.Stayease.Dto.UserRegistrationDto;
import com.crio.Stayease.Service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationDto userdto,BindingResult result){
        if(result.hasErrors()){
            StringBuilder errors=new StringBuilder();
            result.getAllErrors().forEach(error->errors.append(error.getDefaultMessage()).append("\n"));
            return new ResponseEntity<String>(errors.toString(),HttpStatus.BAD_REQUEST);
        }
        userService.registerUser(userdto);
        return new ResponseEntity<String>("User Registered Successfully", HttpStatus.OK);
    }
}
