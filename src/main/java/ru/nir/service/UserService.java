package ru.nir.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.nir.except.BadRequestException;
import ru.nir.except.EmailAlreadyExistsException;
import ru.nir.except.InternalServerException;
import ru.nir.except.ResourceNotFoundException;
import ru.nir.except.UsernameAlreadyExistsException;
import ru.nir.model.InstaUserDetails;
import ru.nir.model.Role;
import ru.nir.model.User;
import ru.nir.repository.UserRepository;

@Service
@Slf4j
public class UserService {


     private PasswordEncoder passwordEncoder;
     private final UserRepository userRepository;
     private AuthenticationManager authenticationManager;
     private JwtTokenManager jwtTokenManager;
     private TotpManager totpManager;

     @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository,
        AuthenticationManager authenticationManager, JwtTokenManager jwtTokenManager,
        TotpManager totpManager) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenManager = jwtTokenManager;
        this.totpManager = totpManager;
    }

    public String loginUser(String username, String password) {
        Authentication authentication = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(username, password));

        User user = userRepository.findUserByUsername(username).get();
        if(user.isMfa()) {
            return "";
        }

        return jwtTokenManager.generateToken(authentication);
    }

    public String verify(String username, String code) {
        User user = userRepository
            .findUserByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException( String.format("username %s", username)));

        if(!totpManager.verifyCode(code, user.getSecret())) {
            throw new BadRequestException("Code is incorrect");
        }

        return Optional.of(user)
            .map(InstaUserDetails::new)
            .map(userDetails -> new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()))
            .map(jwtTokenManager::generateToken)
            .orElseThrow(() ->
                new InternalServerException("unable to generate access token"));
    }

    public User registerUser(User user, Role role) {
        log.info("registering user {}", user.getUsername());

        if(userRepository.existsByUsername(user.getUsername())) {
            log.warn("username {} already exists.", user.getUsername());

            throw new UsernameAlreadyExistsException(
                String.format("username %s already exists", user.getUsername()));
        }

        if(userRepository.existsByEmail(user.getEmail())) {
            log.warn("email {} already exists.", user.getEmail());

            throw new EmailAlreadyExistsException(
                String.format("email %s already exists", user.getEmail()));
        }
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>() {{
            add(role);
        }});

        if(user.isMfa()) {
            user.setSecret(totpManager.generateSecret());
        }

        return userRepository.saveUser(user);
    }

    public List<User> findAll() {
        log.info("retrieving all users");
        return userRepository.findAll();
    }

    public Optional<User> findByUsername(String username) {
        log.info("retrieving user {}", username);
        return userRepository.findUserByUsername(username);
    }

    public Optional<User> findById(String id) {
        log.info("retrieving user {}", id);
        return userRepository.findUserById(id);
    }
}
