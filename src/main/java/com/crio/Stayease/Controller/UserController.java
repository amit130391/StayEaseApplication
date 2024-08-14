package com.crio.Stayease.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crio.Stayease.Entity.User;
import com.crio.Stayease.Service.UserService;

@RestController
public class UserController {
    
    @Autowired
    UserService userService;
 
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/users")
    public ResponseEntity<List<User>> getallUsers(){
        List<User> getallUsers = userService.getallUsers();
        if(getallUsers.isEmpty())
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        
        return new ResponseEntity<List<User>>(getallUsers, HttpStatus.OK);
    }

    @GetMapping("/user/me")
    public ResponseEntity<User> getUser(@AuthenticationPrincipal UserDetails userDetails){
        User user = userService.getUser(userDetails.getUsername());
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }
    
}
