package com.github.Ahmed_Zein.dms.api.controllers.auth;

import com.github.Ahmed_Zein.dms.api.models.LoginBody;
import com.github.Ahmed_Zein.dms.api.models.LoginResponse;
import com.github.Ahmed_Zein.dms.api.models.RegistrationBody;
import com.github.Ahmed_Zein.dms.exception.EmailExistsException;
import com.github.Ahmed_Zein.dms.exception.InvalidOperationException;
import com.github.Ahmed_Zein.dms.exception.UserNotFoundException;
import com.github.Ahmed_Zein.dms.models.LocalUser;
import com.github.Ahmed_Zein.dms.models.dao.LocalUserDAO;
import com.github.Ahmed_Zein.dms.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService,
                          LocalUserDAO localUserDAO) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<LocalUser> register(@RequestBody RegistrationBody body) {
        try {
            return ResponseEntity.ok(userService.register(body));
        } catch (EmailExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginBody body) {
        String jwt = null;
        try {
            jwt = userService.login(body);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InvalidOperationException e) {
            return ResponseEntity.badRequest().build();
        }
        var response = LoginResponse.builder().jwt(jwt).success(true).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<LocalUser> me(@AuthenticationPrincipal LocalUser user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(user);
    }
}
