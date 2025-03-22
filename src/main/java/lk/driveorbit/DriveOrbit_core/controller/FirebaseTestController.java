package lk.driveorbit.DriveOrbit_core.controller;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/firebase-test")
@ConditionalOnBean(Firestore.class)
public class FirebaseTestController {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseTestController.class);

    @Autowired
    private Firestore firestore;

    @GetMapping
    public ResponseEntity<Map<String, Object>> testFirebase() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            logger.info("Testing Firebase connection");
            
            // Create a test document
            Map<String, Object> testData = new HashMap<>();
            testData.put("test", true);
            testData.put("timestamp", System.currentTimeMillis());
            
            // Write to Firestore
            WriteResult result = firestore.collection("test")
                    .document("test-" + System.currentTimeMillis())
                    .set(testData)
                    .get();
            
            logger.info("Firebase test write successful at: {}", result.getUpdateTime());
            
            response.put("status", "success");
            response.put("message", "Firebase connection test succeeded");
            response.put("timestamp", result.getUpdateTime().toString());
            
            return ResponseEntity.ok(response);
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Firebase test failed: {}", e.getMessage(), e);
            
            response.put("status", "error");
            response.put("message", "Firebase test failed: " + e.getMessage());
            
            return ResponseEntity.ok(response);
        }
    }
}
