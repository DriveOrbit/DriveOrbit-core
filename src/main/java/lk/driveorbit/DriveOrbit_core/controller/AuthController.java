package lk.driveorbit.DriveOrbit_core.controller;

import lk.driveorbit.DriveOrbit_core.Service.DriverService;
import lk.driveorbit.DriveOrbit_core.dto.AuthResponse;
import lk.driveorbit.DriveOrbit_core.dto.LoginRequest;
import lk.driveorbit.DriveOrbit_core.model.Driver;
import lk.driveorbit.DriveOrbit_core.security.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final DriverService driverService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(DriverService driverService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.driverService = driverService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<Driver> registerDriver(@RequestBody Driver driver) {
        if (driverService.findByCompanyId(driver.getCompanyID()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(driverService.saveDriver(driver));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginDriver(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getCompanyId(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Optional<Driver> driver = driverService.findByCompanyId(loginRequest.getCompanyId());
            if (driver.isPresent()) {
                String token = jwtTokenProvider.generateToken(driver.get().getCompanyID(), driver.get().getRole());
                return ResponseEntity.ok(new AuthResponse(token, driver.get().getRole()));
            } else {
                return ResponseEntity.status(401).body("Invalid credentials1");
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid credentials2");
        }
    }
}