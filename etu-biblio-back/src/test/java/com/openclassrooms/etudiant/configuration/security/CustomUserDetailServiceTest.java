package com.openclassrooms.etudiant.configuration.security;

import com.openclassrooms.etudiant.entities.User;
import com.openclassrooms.etudiant.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailService customUserDetailService;

    // TEST 1 — utilisateur trouvé → UserDetails correct
    @Test
    void test_loadUserByUsername_returns_UserDetails() {
        // GIVEN
        User user = new User();
        user.setLogin("john");
        user.setPassword("encodedPassword");

        when(userRepository.findByLogin("john")).thenReturn(Optional.of(user));

        // WHEN
        UserDetails result = customUserDetailService.loadUserByUsername("john");

        // THEN
        assertThat(result.getUsername()).isEqualTo("john");
        assertThat(result.getPassword()).isEqualTo("encodedPassword");
        assertThat(result.getAuthorities()).extracting("authority").containsExactly("USER");
    }

    // TEST 2 — utilisateur non trouvé → exception
    @Test
    void test_loadUserByUsername_throws_exception_when_user_not_found() {
        // GIVEN
        when(userRepository.findByLogin("unknown")).thenReturn(Optional.empty());

        // THEN
        assertThatThrownBy(() -> customUserDetailService.loadUserByUsername("unknown"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User Not Found");
    }
}
