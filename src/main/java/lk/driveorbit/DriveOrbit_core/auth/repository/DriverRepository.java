package lk.driveorbit.DriveOrbit_core.auth.repository;

import lk.driveorbit.DriveOrbit_core.auth.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    Driver findByUsername(String username);
}
