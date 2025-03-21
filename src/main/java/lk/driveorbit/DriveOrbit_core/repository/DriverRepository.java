package lk.driveorbit.DriveOrbit_core.repository;

import lk.driveorbit.DriveOrbit_core.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    Driver findByEmail(String email);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM Driver d WHERE d.email = ?1")
    void deleteByEmail(String email);
}
