package lk.driveorbit.DriveOrbit_core.controller;

import com.google.cloud.firestore.Firestore;
import lk.driveorbit.DriveOrbit_core.config.FirebaseInitMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/diagnostics")
@ConditionalOnProperty(name = "app.diagnostics.enabled", havingValue = "true", matchIfMissing = true)
public class DiagnosticsController {

    private static final Logger logger = LoggerFactory.getLogger(DiagnosticsController.class);

    @Autowired(required = false)
    private Firestore firestore;
    
    @Autowired
    private FirebaseInitMonitor firebaseInitMonitor;
    
    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping("/firebase-status")
    public ResponseEntity<Map<String, Object>> getFirebaseStatus() {
        Map<String, Object> status = new HashMap<>();
        
        boolean isFirestoreAvailable = firebaseInitMonitor.isFirestoreAvailable();
        status.put("firestore_available", isFirestoreAvailable);
        
        if (isFirestoreAvailable) {
            status.put("status", "Firestore is properly initialized and available");
            
            // Test connection
            try {
                firestore.collection("test").limit(1).get();
                status.put("connection_test", "SUCCESS");
            } catch (Exception e) {
                status.put("connection_test", "FAILED");
                status.put("error_message", e.getMessage());
            }
        } else {
            status.put("status", "Firestore is NOT available");
            status.put("possible_reasons", new String[]{
                "google-services.json file missing from classpath",
                "Invalid credentials in google-services.json",
                "Network connectivity issues",
                "Firebase project configuration issues"
            });
            
            // Check for credentials file
            status.put("credentials_check", checkCredentialsFiles());
            
            status.put("troubleshooting", Arrays.asList(
                "Make sure google-services.json is in src/main/resources",
                "Check application logs for detailed error messages",
                "Verify Firebase project is properly set up",
                "Ensure the service account has proper permissions"
            ));
        }
        
        logger.info("Diagnostics request for Firebase status: {}", status);
        return ResponseEntity.ok(status);
    }
    
    @GetMapping("/app-info")
    public ResponseEntity<Map<String, Object>> getAppInfo() {
        Map<String, Object> info = new HashMap<>();
        
        // Runtime info
        info.put("java_version", System.getProperty("java.version"));
        info.put("app_dir", Paths.get("").toAbsolutePath().toString());
        info.put("available_processors", Runtime.getRuntime().availableProcessors());
        info.put("free_memory_mb", Runtime.getRuntime().freeMemory() / (1024 * 1024));
        info.put("total_memory_mb", Runtime.getRuntime().totalMemory() / (1024 * 1024));
        
        // Environment
        Map<String, String> env = new HashMap<>();
        for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
            // Filter out sensitive information
            if (!entry.getKey().toLowerCase().contains("key") && 
                !entry.getKey().toLowerCase().contains("secret") &&
                !entry.getKey().toLowerCase().contains("password") &&
                !entry.getKey().toLowerCase().contains("token")) {
                env.put(entry.getKey(), entry.getValue());
            }
        }
        info.put("environment", env);
        
        // Classpath resources
        try {
            List<String> classpathResources = new ArrayList<>();
            Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader)
                    .getResources("classpath:*");
            
            for (Resource resource : resources) {
                classpathResources.add(resource.getFilename());
            }
            info.put("classpath_resources", classpathResources);
        } catch (Exception e) {
            info.put("classpath_resources_error", e.getMessage());
        }
        
        return ResponseEntity.ok(info);
    }
    
    private Map<String, Object> checkCredentialsFiles() {
        Map<String, Object> result = new HashMap<>();
        List<String> found = new ArrayList<>();
        List<String> notFound = new ArrayList<>();
        
        // Common credential filenames
        List<String> credentialsFiles = Arrays.asList(
                "google-services.json",
                "firebase-credentials.json",
                "firebase-adminsdk.json",
                "service-account.json"
        );
        
        // Check classpath
        for (String filename : credentialsFiles) {
            Resource resource = resourceLoader.getResource("classpath:" + filename);
            try {
                if (resource.exists()) {
                    found.add("classpath:" + filename);
                } else {
                    notFound.add("classpath:" + filename);
                }
            } catch (Exception e) {
                notFound.add("classpath:" + filename + " (error: " + e.getMessage() + ")");
            }
        }
        
        // Check current directory
        String currentDir = Paths.get("").toAbsolutePath().toString();
        for (String filename : credentialsFiles) {
            File file = new File(currentDir, filename);
            if (file.exists() && file.isFile()) {
                found.add(file.getAbsolutePath());
            } else {
                notFound.add(file.getAbsolutePath());
            }
        }
        
        result.put("found", found);
        result.put("not_found", notFound);
        return result;
    }
}
