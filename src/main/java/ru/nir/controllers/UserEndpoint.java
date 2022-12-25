package ru.nir.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.nir.except.ResourceNotFoundException;
import ru.nir.model.InstaUserDetails;
import ru.nir.model.UserSumm;
import ru.nir.service.UserService;

@RestController
@Slf4j
public class UserEndpoint {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/users/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findUser(@PathVariable("username") String username) {
        log.info("retrieving user {}", username);

        return  userService
            .findByUsername(username)
            .map(user -> ResponseEntity.ok(user))
            .orElseThrow(() -> new ResourceNotFoundException(username));
    }

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll() {
        log.info("retrieving all users");

        return ResponseEntity
            .ok(userService.findAll());
    }

    @GetMapping(value = "/users/me", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER') or hasRole('FACEBOOK_USER')")
    @ResponseStatus(HttpStatus.OK)
    public UserSumm getCurrentUser(@AuthenticationPrincipal InstaUserDetails userDetails) {
        return UserSumm
            .builder()
            .id(userDetails.getId())
            .username(userDetails.getUsername())
            .name(userDetails.getUserProfile().getDisplayName())
            .profilePicture(userDetails.getUserProfile().getProfilePictureUrl())
            .build();
    }
}