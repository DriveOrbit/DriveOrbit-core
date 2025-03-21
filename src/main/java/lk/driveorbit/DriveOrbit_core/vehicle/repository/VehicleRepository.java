package lk.driveorbit.DriveOrbit_core.vehicle.repository;

import lk.driveorbit.DriveOrbit_core.vehicle.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
}
