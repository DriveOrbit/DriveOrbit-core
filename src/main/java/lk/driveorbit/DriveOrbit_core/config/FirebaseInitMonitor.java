package lk.driveorbit.DriveOrbit_core.config;

import com.google.cloud.firestore.Firestore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class FirebaseInitMonitor implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseInitMonitor.class);
    
    @Autowired(required = false)
    private Firestore firestore;
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (firestore != null) {
            logger.info("Firebase/Firestore initialization STATUS: SUCCESS");
            logger.info("Firestore instance is available in the application context");
            
            // Perform a simple test to verify connection
            try {
                firestore.collection("test").limit(1).get();
                logger.info("Firestore connection test: SUCCESS");
            } catch (Exception e) {
                logger.error("Firestore connection test: FAILED - {}", e.getMessage());
            }
        } else {
            logger.error("Firebase/Firestore initialization STATUS: FAILED");
            logger.error("Firestore instance is NOT available in the application context");
            logger.error("Check that google-services.json is in the classpath and contains valid credentials");
        }
    }
    
    public boolean isFirestoreAvailable() {
        return firestore != null;
    }
}
