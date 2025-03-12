package lk.driveorbit.DriveOrbit_core.repository;

import lk.driveorbit.DriveOrbit_core.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
}
