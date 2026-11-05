package ma.fstg.security.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class CustomUserDetailsServiceTest {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void chargeUnUtilisateurDepuisLaBase() {
        UserDetails admin = userDetailsService.loadUserByUsername("admin");

        assertThat(admin.getUsername()).isEqualTo("admin");
        assertThat(passwordEncoder.matches("1234", admin.getPassword())).isTrue();
        assertThat(admin.getAuthorities())
                .extracting("authority")
                .containsExactlyInAnyOrder("ROLE_USER", "ROLE_ADMIN");
        assertThat(admin.isEnabled()).isTrue();
    }

    @Test
    void refuseUnUtilisateurInconnu() {
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("inconnu"))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}
