package lk.driveorbit.DriveOrbit_core.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.Firestore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;

import java.io.IOException;

@Configuration
public class FirebaseConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(FirebaseConfig.class);
    private static final String SERVICE_ACCOUNT_FILE = "google-services.json";
    
    @Bean
    @ConditionalOnResource(resources = "classpath:google-services.json")
    public Firestore firestore() throws IOException {
        try {
            logger.info("Attempting to initialize Firebase with service account file: {}", SERVICE_ACCOUNT_FILE);
            
            if (FirebaseApp.getApps().isEmpty()) {
                logger.debug("No existing Firebase app found, initializing new one");
                GoogleCredentials credentials = GoogleCredentials.fromStream(
                        new ClassPathResource(SERVICE_ACCOUNT_FILE).getInputStream());
                
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(credentials)
                        .build();
                
                FirebaseApp app = FirebaseApp.initializeApp(options);
                logger.info("Firebase has been initialized successfully: {}", app.getName());
            } else {
                logger.info("Firebase app already initialized");
            }
            
            Firestore firestoreInstance = FirestoreClient.getFirestore();
            // Test connection by getting a collection
            firestoreInstance.collection("test").limit(1).get().get();
            logger.info("Firestore connection test successful");
            
            return firestoreInstance;
        } catch (Exception e) {
            logger.error("Failed to initialize Firebase: {}", e.getMessage(), e);
            throw new IOException("Firebase initialization failed", e);
        }
    }
    
    // Placeholder method for when service account file doesn't exist
    @Bean
    @ConditionalOnResource(resources = "!classpath:google-services.json")
    public void firebaseNotConfigured() {
        logger.warn("Firebase is not configured. The file '{}' was not found in the classpath. " +
                "Firebase functionality will not be available.", SERVICE_ACCOUNT_FILE);
    }
}
