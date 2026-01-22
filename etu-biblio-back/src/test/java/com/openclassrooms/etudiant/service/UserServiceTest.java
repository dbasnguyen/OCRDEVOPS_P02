package com.openclassrooms.etudiant.service;

import com.openclassrooms.etudiant.entities.User;
import com.openclassrooms.etudiant.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.springframework.security.core.userdetails.UserDetails;


@ExtendWith(SpringExtension.class)
public class UserServiceTest {
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String LOGIN = "LOGIN";
    private static final String PASSWORD = "PASSWORD";
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @Test
    public void test_create_null_user_throws_IllegalArgumentException() {
        // GIVEN

        // THEN
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> userService.register(null));
    }

    @Test
    public void test_create_already_exist_user_throws_IllegalArgumentException() {
        // GIVEN
        User user = new User();
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setLogin(LOGIN);
        user.setPassword(PASSWORD);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(PASSWORD);
        when(userRepository.findByLogin(any())).thenReturn(Optional.of(user));

        // THEN
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> userService.register(user));
    }

    @Test
    public void test_create_user() {
        // GIVEN
        User user = new User();
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setLogin(LOGIN);
        user.setPassword(PASSWORD);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(PASSWORD);
        when(userRepository.findByLogin(any())).thenReturn(Optional.empty());

        // WHEN
        userService.register(user);

        // THEN
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertThat(userCaptor.getValue()).isEqualTo(user);
    }

//  AJOUT DE TEST Pour PROJET 2 : EXERCICE 2
@Mock
private JwtService jwtService;


    @Test
    public void test_login_with_null_login_throws_exception() {
        // THEN
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.login(null, "password");
        });
    }

@Test
public void test_login_with_null_password_throws_exception() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
        userService.login("login", null);
    });
}

@Test
public void test_login_with_unknown_login_throws_exception() {
    // GIVEN
    when(userRepository.findByLogin("unknown")).thenReturn(Optional.empty());

    // THEN
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
        userService.login("unknown", "password");
    });
}

// Objectif du test: Vérifier que login() lève une IllegalArgumentException si le mot de passe fourni est incorrect.
@Test
public void test_login_with_wrong_password_throws_exception() {
    // GIVEN
    User user = new User();
    user.setLogin("login");
    user.setPassword("encodedPassword");

    when(userRepository.findByLogin("login")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

    // THEN
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
        userService.login("login", "wrongPassword");
    });
}


// ÉTAPE 3 — TEST 5 : login correct → token généré

// Coorigé apres erreur
@Test
public void test_login_success_returns_token() {
    // GIVEN
    User user = new User();
    user.setLogin("login");
    user.setPassword("encodedPassword");

    when(userRepository.findByLogin("login")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
    when(jwtService.generateToken(any())).thenReturn("fakeToken");

    // WHEN
    String token = userService.login("login", "password");

    // THEN
    assertThat(token).isNotNull();
    assertThat(token).isNotEmpty();
}

// ÉTAPE 3 — TEST 6 : UserService.findAll()
@Test
public void test_findAll_returns_user_list() {
    // GIVEN
    User user1 = new User();
    user1.setLogin("user1");

    User user2 = new User();
    user2.setLogin("user2");

    List<User> users = List.of(user1, user2);

    when(userRepository.findAll()).thenReturn(users);

    // WHEN
    List<User> result = userService.findAll();

    // THEN
    assertThat(result).hasSize(2);
    assertThat(result).containsExactly(user1, user2);
}





}
