package lk.driveorbit.DriveOrbit_core.controller;

import lk.driveorbit.DriveOrbit_core.dto.AuthResponse;
import lk.driveorbit.DriveOrbit_core.dto.UserIdRequest;
import lk.driveorbit.DriveOrbit_core.model.Driver;
import lk.driveorbit.DriveOrbit_core.security.JwtTokenProvider;
import lk.driveorbit.DriveOrbit_core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
;

import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<Driver> registerUser(@RequestBody Driver user) {
        if (userService.findByUserID(user.getUserID()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userService.saveUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Driver loginUser) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUser.getUserID(), loginUser.getPassword())
            );

            // if authentication is successful, set the authentication in the security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // get the authenticated user
            String currentUsername = authentication.getName();
            Optional<Driver> user = userService.findByUserID(currentUsername);

            if (user.isPresent()) {
                // create a response with the token and role
                String role = user.get().getRole();
                String token = JwtTokenProvider.generateToken(currentUsername, role);

                return ResponseEntity.ok(new AuthResponse(token, role));
            }

            return ResponseEntity.badRequest().body("Invalid company ID or password");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid company ID or password");
        }
    }


    @PostMapping("/get-email")
    public ResponseEntity<String> getEmailByUserId(@RequestBody UserIdRequest userIdRequest) {
        String userId = userIdRequest.getUserId();
        Optional<Driver> user = userService.findByUserID(userId);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get().getEmail());
        } else {
            return ResponseEntity.badRequest().body("User not found");
        }
    }
}