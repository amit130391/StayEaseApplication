package com.crio.Stayease.Service;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.crio.Stayease.Dto.UserRegistrationDto;
import com.crio.Stayease.Entity.User;
import com.crio.Stayease.Entity.enums.Role;
import com.crio.Stayease.Exception.EmailAlreadyInUseException;
import com.crio.Stayease.Repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser_Success() {
        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("john.doe@example.com");
        userDto.setPassword("password");

        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(new User());

        User registeredUser = userService.registerUser(userDto);

        assertNotNull(registeredUser);
        assertEquals("John", registeredUser.getFirstName());
        assertEquals("Doe", registeredUser.getLastName());
        assertEquals("john.doe@example.com", registeredUser.getEmail());
        assertEquals("encodedPassword", registeredUser.getPassword());
        assertEquals(Role.CUSTOMER, registeredUser.getRole());
    }

    @Test
    public void testRegisterUser_EmailAlreadyInUse() {
        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("john.doe@example.com");
        userDto.setPassword("password");

        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenThrow(new DataIntegrityViolationException(""));

        EmailAlreadyInUseException exception = assertThrows(EmailAlreadyInUseException.class, () -> {
            userService.registerUser(userDto);
        });

        assertEquals("Email is already in use: john.doe@example.com", exception.getMessage());
    }

    @Test
    public void testGetAllUsers() {
        List<User> mockUsers = new ArrayList<>();
        mockUsers.add(new User());
        when(userRepository.findAll()).thenReturn(mockUsers);

        List<User> users = userService.getallUsers();

        assertNotNull(users);
        assertEquals(1, users.size());
    }

    @Test
    public void testGetUser_Success() {
        User mockUser = new User();
        mockUser.setEmail("john.doe@example.com");
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(mockUser);

        User user = userService.getUser("john.doe@example.com");

        assertNotNull(user);
        assertEquals("john.doe@example.com", user.getEmail());
    }

    @Test
    public void testGetUser_NotFound() {
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(null);

        User user = userService.getUser("john.doe@example.com");

        assertNull(user);
    }
}

