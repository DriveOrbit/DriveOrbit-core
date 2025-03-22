package lk.driveorbit.DriveOrbit_core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"lk.driveorbit.DriveOrbit_core"})
public class DriveOrbitCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(DriveOrbitCoreApplication.class, args);
	}

}
