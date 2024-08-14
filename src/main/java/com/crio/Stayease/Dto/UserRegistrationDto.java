package com.crio.Stayease.Dto;

import com.crio.Stayease.Entity.enums.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDto {
    @Email
    @NotEmpty(message = "Email is mandatory")
    private String email;

    @Size(min = 8,message="Password should have atleast 8 characters")
    @NotEmpty(message = "Password is mandatory")
    private String password;

    @NotEmpty(message = "First name is mandatory")
    private String firstName;

    private String lastName;

    private Role role;


}

