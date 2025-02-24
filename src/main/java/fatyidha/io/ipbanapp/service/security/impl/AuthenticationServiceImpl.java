package fatyidha.io.ipbanapp.service.security.impl;

import fatyidha.io.ipbanapp.dto.request.LoginRequestDto;
import fatyidha.io.ipbanapp.dto.request.RegisterRequestDto;
import fatyidha.io.ipbanapp.dto.response.JwtAuthenticationResponseDto;
import fatyidha.io.ipbanapp.model.User;
import fatyidha.io.ipbanapp.repository.UserRepository;
import fatyidha.io.ipbanapp.service.security.AuthenticationService;
import fatyidha.io.ipbanapp.service.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtAuthenticationResponseDto register(RegisterRequestDto registerRequestDto, String userIpAddress) throws Exception {
        checkIfEmailExists(registerRequestDto.getEmail());
        var user = User.builder().firstName(registerRequestDto.getFirstName()).lastName(registerRequestDto.getLastName()).username(registerRequestDto.getUsername())
                .email(registerRequestDto.getEmail()).password(passwordEncoder.encode(registerRequestDto.getPassword())).ipAddress(userIpAddress).isActive(true).build();

        userRepository.save(user);

        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponseDto.builder().token(jwt).build();
    }

    @Override
    public JwtAuthenticationResponseDto login(LoginRequestDto loginRequestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));
        var user = userRepository.findByUsername(loginRequestDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponseDto.builder().token(jwt).build();
    }

    private void checkIfEmailExists(String email) throws Exception {
        if(userRepository.existsByEmail(email)){
            throw new Exception("Email already exists");
        }
    }
}
