package com.luxuryproductsholding.api.controllers;

import com.luxuryproductsholding.api.models.CustomUser;
import com.luxuryproductsholding.api.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable long id) {
        CustomUser authenticatedUser = userService.getAuthenticatedUser();

        if (authenticatedUser == null) {
            return ResponseEntity.status(401).body("Niet ingelogd.");
        }

        if (!userService.isUserAuthorized(authenticatedUser, id)) {
            return ResponseEntity.status(403).body("Geen toegang tot deze gebruiker.");
        }

        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
