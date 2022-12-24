package ru.nir.controllers;

import java.net.URI;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.nir.except.BadRequestException;
import ru.nir.except.EmailAlreadyExistsException;
import ru.nir.except.UsernameAlreadyExistsException;
import ru.nir.model.JwtAuthenticationResponse;
import ru.nir.model.Profile;
import ru.nir.model.Role;
import ru.nir.model.SignupResponse;
import ru.nir.model.User;
import ru.nir.model.login.LoginRequest;
import ru.nir.model.login.VerifyCodeRequest;
import ru.nir.model.registr.SignRequestUser;
import ru.nir.service.TotpManager;
import ru.nir.service.UserService;

@RestController
@Slf4j
public class AuthController {

    private final UserService userService;
    private final TotpManager totpManager;


    @Autowired
    public AuthController(UserService userService,
        TotpManager totpManager) {
        this.userService = userService;
        this.totpManager = totpManager;
    }

    /**
     * ResponseEntity - класс для возврата ответов. С помощью него мы сможем в дальнейшем вернуть клиенту HTTP статус код.
     * Метод принимает параметр @RequestBody Client client, значение этого параметра подставляется из тела запроса. Об этом говорит аннотация @RequestBody. Внутри тела метода мы вызываем метод create у ранее созданного сервиса и передаем ему принятого в параметрах контроллера клиента.
     * @param loginRequest
     * @return ResponseEntity
     */

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        String token = userService.loginUser(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok(new JwtAuthenticationResponse(token, StringUtils.isEmpty(token)));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(@Valid @RequestBody VerifyCodeRequest verifyCodeRequest) {
        String token = userService.verify(verifyCodeRequest.getUsername(), verifyCodeRequest.getCode());
        return ResponseEntity.ok(new JwtAuthenticationResponse(token, StringUtils.isEmpty(token)));
    }

    @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@Valid @RequestBody SignRequestUser payload) {
        log.info("creating user {}", payload.getUsername());

        User user = User
            .builder()
            .username(payload.getUsername())
            .email(payload.getEmail())
            .password(payload.getPassword())
            .userProfile(Profile
                .builder()
                .displayName(payload.getName())
                .build())
            .mfa(payload.isMfa())
            .build();

        User saved;
        try {
            saved = userService.registerUser(user, Role.USER);
        } catch (UsernameAlreadyExistsException | EmailAlreadyExistsException e) {
            throw new BadRequestException(e.getMessage());
        }

        URI location = ServletUriComponentsBuilder
            .fromCurrentContextPath().path("/users/{username}")
            .buildAndExpand(user.getUsername()).toUri();

        return ResponseEntity
            .created(location)
            .body(new SignupResponse(saved.isMfa(),
                totpManager.getUriForImage(saved.getSecret())));
    }
}
