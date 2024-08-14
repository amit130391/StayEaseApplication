package com.crio.Stayease.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crio.Stayease.Dto.UserRegistrationDto;
import com.crio.Stayease.Entity.User;
import com.crio.Stayease.Entity.enums.Role;
import com.crio.Stayease.Exception.EmailAlreadyInUseException;
import com.crio.Stayease.Repository.UserRepository;

@Service
public class UserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    public User registerUser(UserRegistrationDto userdto){
        User user = new User();
        user.setFirstName(userdto.getFirstName());
        user.setLastName(userdto.getLastName());
        user.setEmail(userdto.getEmail());
        user.setPassword(passwordEncoder.encode(userdto.getPassword()));
        if(userdto.getRole()==null)
        user.setRole(Role.CUSTOMER);
        else
        user.setRole(userdto.getRole());

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyInUseException("Email is already in use: "+userdto.getEmail());
        }
        return user;
    }

    public List<User> getallUsers(){
        List<User> users = userRepository.findAll(); 
        return users;
    }

    public User getUser(String email){
        User user = userRepository.findByEmail(email);
        return user;
    }
}
