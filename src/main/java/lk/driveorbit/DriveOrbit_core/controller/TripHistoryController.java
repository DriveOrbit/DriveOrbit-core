package lk.driveorbit.DriveOrbit_core.controller;

import lk.driveorbit.DriveOrbit_core.entity.TripHistory;
import lk.driveorbit.DriveOrbit_core.service.TripHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/trip-history")
@CrossOrigin(origins = "*")
public class TripHistoryController {

    @Autowired
    private TripHistoryService tripHistoryService;

    @PostMapping
    public TripHistory saveTripHistory(@RequestBody TripHistory tripHistory) {
        return tripHistoryService.saveTripHistory(tripHistory);
    }

    @GetMapping("/{id}")
    public Optional<TripHistory> getTripHistoryById(@PathVariable String id) {
        return tripHistoryService.getTripHistoryById(id);
    }

    @GetMapping
    public List<TripHistory> getAllTripHistories() {
        return tripHistoryService.getAllTripHistories();
    }
}
