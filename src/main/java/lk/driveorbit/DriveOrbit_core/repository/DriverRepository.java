package lk.driveorbit.DriveOrbit_core.repository;

import lk.driveorbit.DriveOrbit_core.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    Driver findByEmail(String email);
    void deleteByEmail(String email);
}
