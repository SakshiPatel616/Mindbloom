package com.mindbloom.service;

import com.mindbloom.dto.AuthDtos;
import com.mindbloom.dto.UserProfileDto;
import com.mindbloom.entity.User;
import com.mindbloom.repository.UserRepository;
import com.mindbloom.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthDtos.AuthResponse register(AuthDtos.RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .displayName(request.getDisplayName())
                .anonymousMode(request.isAnonymousMode())
                .role(User.Role.USER)
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());
        return buildAuthResponse(token, user);
    }

    public AuthDtos.AuthResponse login(AuthDtos.LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setLastActiveAt(LocalDateTime.now());
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());

        return buildAuthResponse(token, user);
    }

    private AuthDtos.AuthResponse buildAuthResponse(String token, User user) {
        UserProfileDto profile = UserProfileDto.builder()
                .id(user.getId())
                .displayName(user.getDisplayName())
                .email(user.isAnonymousMode() ? "hidden" : user.getEmail())
                .healingPath(user.getHealingPath())
                .anonymousMode(user.isAnonymousMode())
                .onboardingCompleted(user.isOnboardingCompleted())
                .createdAt(user.getCreatedAt())
                .build();

        return AuthDtos.AuthResponse.builder()
                .accessToken(token)
                .user(profile)
                .build();
    }
}