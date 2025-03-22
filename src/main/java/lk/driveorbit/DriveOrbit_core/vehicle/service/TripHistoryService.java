package lk.driveorbit.DriveOrbit_core.vehicle.service;

import com.google.cloud.firestore.Firestore;
import lk.driveorbit.DriveOrbit_core.vehicle.entity.TripHistory;
import lk.driveorbit.DriveOrbit_core.vehicle.repository.TripHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class TripHistoryService {

    private static final Logger logger = LoggerFactory.getLogger(TripHistoryService.class);
    private static final String COLLECTION_NAME = "tripHistory";

    @Autowired
    private TripHistoryRepository tripHistoryRepository;
    
    @Autowired(required = false)
    private Firestore firestore;

    public TripHistory saveTripHistory(TripHistory tripHistory) {
        // Save to PostgreSQL
        TripHistory savedTrip = tripHistoryRepository.save(tripHistory);
        
        // Save to Firebase if configured
        if (firestore != null) {
            try {
                firestore.collection(COLLECTION_NAME)
                        .document(savedTrip.getHistoryId())
                        .set(savedTrip)
                        .get();
                logger.info("Trip history saved to Firebase with ID: {}", savedTrip.getHistoryId());
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Failed to save trip history to Firebase: {}", e.getMessage());
            }
        }
        
        return savedTrip;
    }

    public Optional<TripHistory> getTripHistoryById(String id) {
        return tripHistoryRepository.findById(id);
    }

    public List<TripHistory> getAllTripHistories() {
        return tripHistoryRepository.findAll();
    }
}
