package lk.driveorbit.DriveOrbit_core.service;

import lk.driveorbit.DriveOrbit_core.model.Driver;
import lk.driveorbit.DriveOrbit_core.model.OtpVerification;
import lk.driveorbit.DriveOrbit_core.repository.OtpVerificationRepository;
import lk.driveorbit.DriveOrbit_core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final OtpVerificationRepository otpVerificationRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, OtpVerificationRepository otpVerificationRepository) {
        this.userRepository = userRepository;
        this.otpVerificationRepository = otpVerificationRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Driver saveUser(Driver user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<Driver> findByUserID(String userID) {
        return Optional.ofNullable(userRepository.findByUserID(userID));
    }

    public void saveOTP(String email, String otp) {
        OtpVerification otpVerification = new OtpVerification();
        otpVerification.setEmail(email);
        otpVerification.setOtp(otp);
        otpVerification.setExpiresAt(LocalDateTime.now().plusMinutes(5)); // Set OTP expiration time
        otpVerificationRepository.save(otpVerification);
    }

    @Override
    public UserDetails loadUserByUsername(String userID) throws UsernameNotFoundException {
        Driver user = userRepository.findByUserID(userID);
        if (user == null) {
            throw new UsernameNotFoundException("Driver not found");
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUserID())
                .password(user.getPassword())
                .authorities("USER")
                .build();
    }
}