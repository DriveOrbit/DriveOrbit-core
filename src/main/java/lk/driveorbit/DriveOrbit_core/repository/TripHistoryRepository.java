package lk.driveorbit.DriveOrbit_core.repository;

import lk.driveorbit.DriveOrbit_core.entity.TripHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripHistoryRepository extends JpaRepository<TripHistory, String> {
}
