package lk.driveorbit.DriveOrbit_core.admin.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for the admin module
 * Ensures all admin components are properly scanned and loaded
 */
@Configuration
@ComponentScan(basePackages = "lk.driveorbit.DriveOrbit_core.admin")
public class AdminConfig {
    
    // This configuration class ensures that all admin components
    // are properly scanned and available for dependency injection
    
}
