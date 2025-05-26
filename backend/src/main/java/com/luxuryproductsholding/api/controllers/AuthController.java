package com.luxuryproductsholding.api.controllers;


import com.luxuryproductsholding.api.DAO.RoleRepository;
import com.luxuryproductsholding.api.DAO.UserRepository;
import com.luxuryproductsholding.api.DTO.AuthenticationDTO;
import com.luxuryproductsholding.api.DTO.LoginResponse;
import com.luxuryproductsholding.api.config.JWTUtil;
import com.luxuryproductsholding.api.models.CustomUser;
import com.luxuryproductsholding.api.models.Role;
import com.luxuryproductsholding.api.services.CredentialValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userDAO;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;
    private final CredentialValidator validator;
    private final RoleRepository roleRepository;

    public AuthController(UserRepository userDAO, JWTUtil jwtUtil, AuthenticationManager authManager,
                          PasswordEncoder passwordEncoder, CredentialValidator validator,
                          RoleRepository roleRepository) {
        this.userDAO = userDAO;
        this.jwtUtil = jwtUtil;
        this.authManager = authManager;
        this.passwordEncoder = passwordEncoder;
        this.validator = validator;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody AuthenticationDTO authenticationDTO) {
        if (!validator.isValidEmail(authenticationDTO.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "No valid email provided"
            );
        }

        if (!validator.isValidPassword(authenticationDTO.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "No valid password provided"
            );
        }

        CustomUser customUser = userDAO.findByEmail(authenticationDTO.getEmail());

        if (customUser != null){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Can not register with this email"
            );
        }


        String encodedPassword = passwordEncoder.encode(authenticationDTO.getPassword());

        // Fetch the customer role from the database
        Role customerRole = roleRepository.findByName("customer");
        if (customerRole == null) {
            customerRole = new Role("customer");
            roleRepository.save(customerRole);
        }

        // Assign the customer role to the new user
        Set<Role> roles = new HashSet<>();
        roles.add(customerRole);


        CustomUser registeredCustomUser = new CustomUser(authenticationDTO.getEmail(), authenticationDTO.getPassword(), encodedPassword);
        registeredCustomUser.setRoles(roles);

        // Log the user details for debugging
        //System.out.println("DEBUG: logging Registered user details: " + registeredCustomUser.getEmail() + ", password: " + registeredCustomUser.getPassword() + ", Roles: " + registeredCustomUser.getRoles());


        // Save the new user to the database
        userDAO.save(registeredCustomUser);
        String token = jwtUtil.generateToken(registeredCustomUser.getEmail());
        LoginResponse loginResponse = new LoginResponse(registeredCustomUser.getEmail(), registeredCustomUser.getRoles(), token);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody AuthenticationDTO body) {
        try {
            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(body.getEmail(), body.getPassword());

            authManager.authenticate(authInputToken);

            String token = jwtUtil.generateToken(body.getEmail());

            CustomUser customUser = userDAO.findByEmail(body.getEmail());
            LoginResponse loginResponse = new LoginResponse(customUser.getEmail(), customUser.getRoles(), token);


            return ResponseEntity.ok(loginResponse);

        } catch (AuthenticationException authExc) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "No valid credentials"
            );
        }
    }

}
