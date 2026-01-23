package com.openclassrooms.etudiant.configuration.security;

import com.openclassrooms.etudiant.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private CustomUserDetailService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setup() {
        SecurityContextHolder.clearContext();
    }

    // TEST 1 — Pas de header Authorization → le filtre laisse passer
    @Test
    void test_no_authorization_header() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    // TEST 2 — Token invalide → pas d’authentification
    @Test
    void test_invalid_token() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer fakeToken");
        when(jwtService.extractUsername("fakeToken")).thenReturn("john");

        UserDetails userDetails = User.withUsername("john")
                .password("pass")
                .authorities("USER")
                .build();

        when(userDetailsService.loadUserByUsername("john")).thenReturn(userDetails);
        when(jwtService.isTokenValid("fakeToken", userDetails)).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    // TEST 3 — Token valide → authentification ajoutée dans SecurityContext
    @Test
    void test_valid_token_sets_authentication() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtService.extractUsername("validToken")).thenReturn("john");

        UserDetails userDetails = User.withUsername("john")
                .password("pass")
                .authorities("USER")
                .build();

        when(userDetailsService.loadUserByUsername("john")).thenReturn(userDetails);
        when(jwtService.isTokenValid("validToken", userDetails)).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);

        assertThat(SecurityContextHolder.getContext().getAuthentication())
                .isInstanceOf(UsernamePasswordAuthenticationToken.class);

        assertThat(SecurityContextHolder.getContext().getAuthentication().getName())
                .isEqualTo("john");
    }
}
