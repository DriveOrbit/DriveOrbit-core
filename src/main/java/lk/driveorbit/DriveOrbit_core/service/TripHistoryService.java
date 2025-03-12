package lk.driveorbit.DriveOrbit_core.service;

import lk.driveorbit.DriveOrbit_core.entity.TripHistory;
import lk.driveorbit.DriveOrbit_core.repository.TripHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TripHistoryService {

    @Autowired
    private TripHistoryRepository tripHistoryRepository;

    public TripHistory saveTripHistory(TripHistory tripHistory) {
        return tripHistoryRepository.save(tripHistory);
    }

    public Optional<TripHistory> getTripHistoryById(String id) {
        return tripHistoryRepository.findById(id);
    }

    public List<TripHistory> getAllTripHistories() {
        return tripHistoryRepository.findAll();
    }
}
