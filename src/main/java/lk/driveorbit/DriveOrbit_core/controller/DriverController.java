package lk.driveorbit.DriveOrbit_core.controller;

import lk.driveorbit.DriveOrbit_core.auth.dto.DriverDTO;
import lk.driveorbit.DriveOrbit_core.auth.dto.LoginRequest;
import lk.driveorbit.DriveOrbit_core.auth.model.Driver;
import lk.driveorbit.DriveOrbit_core.auth.services.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @PostMapping("/register")
    public Driver registerDriver(@RequestBody DriverDTO driverDTO) {
        Driver driver = new Driver();
        driver.setCompanyId(driverDTO.getCompanyId());
        driver.setFirstName(driverDTO.getFirstName());
        driver.setLastName(driverDTO.getLastName());
        driver.setUsername(driverDTO.getUsername());
        driver.setPassword(driverDTO.getPassword());
        return driverService.registerDriver(driver);
    }

    @PostMapping("/login")
    public Driver loginDriver(@RequestBody LoginRequest loginRequest) {
        return driverService.loginDriver(loginRequest.getUsername(), loginRequest.getPassword());
    }
}
