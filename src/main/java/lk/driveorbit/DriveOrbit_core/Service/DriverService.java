package lk.driveorbit.DriveOrbit_core.Service;

    import lk.driveorbit.DriveOrbit_core.model.Driver;
    import lk.driveorbit.DriveOrbit_core.repository.UserRepository;
    import lk.driveorbit.DriveOrbit_core.security.JwtTokenProvider;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;

    import java.util.Optional;

    @Service
    public class DriverService implements UserDetailsService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtTokenProvider jwtTokenProvider;

        @Autowired
        public DriverService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
            this.userRepository = userRepository;
            this.passwordEncoder = new BCryptPasswordEncoder();
            this.jwtTokenProvider = jwtTokenProvider;
        }

        public Driver saveDriver(Driver driver) {
            driver.setPassword(passwordEncoder.encode(driver.getPassword()));
            return userRepository.save(driver);
        }

        public Optional<Driver> findByCompanyId(String companyId) {
            return Optional.ofNullable(userRepository.findByCompanyID(companyId));
        }

        public String loginDriver(String companyId, String password) {
            Optional<Driver> driverOpt = findByCompanyId(companyId);
            if (driverOpt.isPresent()) {
                Driver driver = driverOpt.get();
                if (passwordEncoder.matches(password, driver.getPassword())) {
                    return jwtTokenProvider.generateToken(driver.getCompanyID(), driver.getRole());
                }
            }
            return null;
        }

        @Override
        public UserDetails loadUserByUsername(String companyId) throws UsernameNotFoundException {
            Driver driver = userRepository.findByCompanyID(companyId);
            if (driver == null) {
                throw new UsernameNotFoundException("Driver not found");
            }
            return org.springframework.security.core.userdetails.User
                    .withUsername(driver.getCompanyID())
                    .password(driver.getPassword())
                    .authorities(driver.getRole())
                    .build();
        }
    }