package lk.driveorbit.DriveOrbit_core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class FirebaseCredentialsChecker {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseCredentialsChecker.class);
    private static final List<String> CREDENTIALS_FILENAMES = Arrays.asList(
            "google-services.json",
            "firebase-credentials.json",
            "firebase-adminsdk.json",
            "service-account.json"
    );
    
    @Autowired
    private ResourceLoader resourceLoader;
    
    @PostConstruct
    public void checkCredentials() {
        try {
            logger.info("Checking for Firebase credentials files...");
            
            // Check classpath locations
            checkClasspathLocations();
            
            // Check common file system locations
            checkFileSystemLocations();
            
        } catch (Exception e) {
            logger.error("Error during Firebase credentials check: {}", e.getMessage(), e);
        }
    }
    
    private void checkClasspathLocations() throws IOException {
        boolean found = false;
        
        for (String filename : CREDENTIALS_FILENAMES) {
            Resource resource = resourceLoader.getResource("classpath:" + filename);
            if (resource.exists()) {
                logger.info("FOUND Firebase credentials in classpath: {}", filename);
                found = true;
            }
        }
        
        // Check src/main/resources directory contents
        Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader)
                .getResources("classpath:*");
        
        logger.info("Resources in classpath root:");
        for (Resource resource : resources) {
            logger.info(" - {}", resource.getFilename());
        }
        
        if (!found) {
            logger.warn("No Firebase credentials files found in classpath");
        }
    }
    
    private void checkFileSystemLocations() {
        List<String> commonPaths = new ArrayList<>();
        
        // Get current directory
        String currentDir = Paths.get("").toAbsolutePath().toString();
        commonPaths.add(currentDir);
        
        // Add config directory if it exists
        File configDir = new File("config");
        if (configDir.exists() && configDir.isDirectory()) {
            commonPaths.add(configDir.getAbsolutePath());
        }
        
        // Check src/main/resources
        File resourcesDir = new File("src/main/resources");
        if (resourcesDir.exists() && resourcesDir.isDirectory()) {
            commonPaths.add(resourcesDir.getAbsolutePath());
        }
        
        // Check each location
        for (String path : commonPaths) {
            logger.info("Checking directory for Firebase credentials: {}", path);
            
            for (String filename : CREDENTIALS_FILENAMES) {
                File credFile = new File(path, filename);
                if (credFile.exists() && credFile.isFile()) {
                    logger.info("FOUND Firebase credentials file: {}", credFile.getAbsolutePath());
                }
            }
        }
    }
}
