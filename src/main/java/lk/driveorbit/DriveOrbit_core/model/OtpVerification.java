package lk.driveorbit.DriveOrbit_core.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class OtpVerification {
    @Id
    private String email;
    private String otp;
    private LocalDateTime expiresAt;
}