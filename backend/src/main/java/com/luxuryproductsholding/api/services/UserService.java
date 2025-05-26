package com.luxuryproductsholding.api.services;

import com.luxuryproductsholding.api.DAO.UserRepository;
import com.luxuryproductsholding.api.models.CustomUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userDAO) {
        this.userRepository = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        CustomUser customUser = userRepository.findByEmail(email);
        if (customUser == null) {
            throw new UsernameNotFoundException("Gebruiker niet gevonden met e-mail: " + email);
        }

        List<SimpleGrantedAuthority> authorities = customUser.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();

        return new User(
                customUser.getEmail(),
                customUser.getPassword(),
                authorities
        );
    }

    public CustomUser getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userRepository.findByEmail(userDetails.getUsername());
        }
        return null;
    }

    public boolean isUserAuthorized(CustomUser authenticatedUser, long id) {
        return authenticatedUser != null &&
                (authenticatedUser.getId() == id);
    }

    public Optional<CustomUser> getUserById(long id) {
        return userRepository.findById(id);
    }
}
