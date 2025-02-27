package lk.driveorbit.DriveOrbit_core.repository;

import lk.driveorbit.DriveOrbit_core.model.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpVerificationRepository extends JpaRepository<OtpVerification, String> {
}