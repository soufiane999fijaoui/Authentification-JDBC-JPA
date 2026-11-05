package ma.fstg.security.services;

import ma.fstg.security.entities.User;
import ma.fstg.security.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void inscritUnUtilisateurAvecUnMotDePasseBCrypt() {
        userService.register("nouveau", "secret123");

        User saved = userRepository.findByUsername("nouveau").orElseThrow();
        assertThat(saved.getPassword()).isNotEqualTo("secret123");
        assertThat(saved.getPassword()).startsWith("$2");
        assertThat(passwordEncoder.matches("secret123", saved.getPassword())).isTrue();
        assertThat(saved.isActive()).isTrue();
        assertThat(saved.getRoles())
                .extracting("name")
                .containsExactly("ROLE_USER");
    }

    @Test
    void refuseUnNomDUtilisateurDejaUtilise() {
        assertThatThrownBy(() -> userService.register("admin", "autre123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("déjà utilisé");
    }
}
