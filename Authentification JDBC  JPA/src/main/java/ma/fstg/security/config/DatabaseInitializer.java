package ma.fstg.security.config;

import lombok.extern.slf4j.Slf4j;
import ma.fstg.security.entities.Role;
import ma.fstg.security.entities.User;
import ma.fstg.security.repositories.RoleRepository;
import ma.fstg.security.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class DatabaseInitializer implements CommandLineRunner {

    private final RoleRepository roleRepo;
    private final UserRepository userRepo;
    private final BCryptPasswordEncoder encoder;

    public DatabaseInitializer(RoleRepository roleRepo, UserRepository userRepo, BCryptPasswordEncoder encoder) {
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Role adminRole = findOrCreateRole("ROLE_ADMIN");
        Role userRole = findOrCreateRole("ROLE_USER");

        createUserIfMissing("admin", "1234", true, List.of(adminRole, userRole));
        createUserIfMissing("user", "1111", true, List.of(userRole));

        log.info("Comptes de demonstration disponibles : admin/1234 (ADMIN + USER) et user/1111 (USER)");
    }

    private Role findOrCreateRole(String name) {
        Role role = roleRepo.findByName(name);
        return role != null ? role : roleRepo.save(new Role(null, name));
    }

    private void createUserIfMissing(String username, String rawPassword, boolean active, List<Role> roles) {
        if (userRepo.existsByUsername(username)) {
            return;
        }

        User user = new User(null, username, encoder.encode(rawPassword), active, new ArrayList<>(roles));
        userRepo.save(user);
    }
}
