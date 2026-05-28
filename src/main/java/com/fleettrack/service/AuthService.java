package com.fleettrack.service;

import com.fleettrack.dto.request.LoginRequest;
import com.fleettrack.dto.request.RegisterRequest;
import com.fleettrack.dto.response.AuthResponse;
import com.fleettrack.entity.User;
import com.fleettrack.repository.UserRepository;
import com.fleettrack.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ===== REGISTER =====
    public AuthResponse register(RegisterRequest request) {

        // 1. Username mövcuddurmu yoxla
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException(
                    "Bu username artıq mövcuddur: " + request.getUsername()
            );
        }

        // 2. Email mövcuddurmu yoxla
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException(
                    "Bu email artıq mövcuddur: " + request.getEmail()
            );
        }

        // 3. İstifadəçini yarat
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(request.getRole())
                .build();

        // 4. DB-yə yaz
        userRepository.save(user);

        // 5. UserDetails yüklə
        UserDetails userDetails =
                userDetailsService.loadUserByUsername(request.getUsername());

        // 6. Token yarat və qaytar
        String token = jwtService.generateToken(userDetails);

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole().name())
                .expiresIn(jwtService.getExpiration())
                .build();
    }

    public AuthResponse login(LoginRequest request) {

        // 1. Username və şifrəni yoxla
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // 2. İstifadəçini yüklə
        UserDetails userDetails =
                userDetailsService.loadUserByUsername(request.getUsername());

        // 3. Token yarat
        String token = jwtService.generateToken(userDetails);

        // 4. Cavab qaytar
        return AuthResponse.builder()
                .token(token)
                .username(userDetails.getUsername())
                .role(userDetails.getAuthorities()
                        .iterator().next().getAuthority())
                .expiresIn(jwtService.getExpiration())
                .build();
    }
}
