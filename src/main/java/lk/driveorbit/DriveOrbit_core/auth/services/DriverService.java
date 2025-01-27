package lk.driveorbit.DriveOrbit_core.auth.services;

import lk.driveorbit.DriveOrbit_core.auth.model.Driver;
import lk.driveorbit.DriveOrbit_core.auth.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Driver registerDriver(Driver driver) {
        driver.setPassword(passwordEncoder.encode(driver.getPassword()));
        return driverRepository.save(driver);
    }

    public Driver loginDriver(String username, String password) {
        Driver driver = driverRepository.findByUsername(username);
        if (driver != null && passwordEncoder.matches(password, driver.getPassword())) {
            return driver;
        }
        return null;
    }

}
