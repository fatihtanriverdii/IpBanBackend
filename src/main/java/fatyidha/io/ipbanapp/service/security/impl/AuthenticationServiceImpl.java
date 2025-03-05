package fatyidha.io.ipbanapp.service.security.impl;

import fatyidha.io.ipbanapp.dto.request.LoginRequestDto;
import fatyidha.io.ipbanapp.dto.request.RegisterRequestDto;
import fatyidha.io.ipbanapp.dto.response.JwtAuthenticationResponseDto;
import fatyidha.io.ipbanapp.model.IpAddress;
import fatyidha.io.ipbanapp.model.Role;
import fatyidha.io.ipbanapp.model.User;
import fatyidha.io.ipbanapp.service.security.AuthenticationService;
import fatyidha.io.ipbanapp.service.security.IpAddressService;
import fatyidha.io.ipbanapp.service.security.JwtService;
import fatyidha.io.ipbanapp.service.security.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public JwtAuthenticationResponseDto register(RegisterRequestDto registerRequestDto, String userIpAddress) throws Exception {
        checkIfEmailExists(registerRequestDto.getEmail());

        User user = new User();
        user.setFirstName(registerRequestDto.getFirstName());
        user.setLastName(registerRequestDto.getLastName());
        user.setUsername(registerRequestDto.getUsername());
        user.setEmail(registerRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));

        var jwt = jwtService.generateToken(user);
        user.setToken(jwt);
        user.setTokenExpiryDate(jwtService.extractExpiration(jwt));

        addIpAddress(user, userIpAddress);

        return JwtAuthenticationResponseDto.builder().token(jwt).build();
    }

    @Override
    @Transactional
    public JwtAuthenticationResponseDto login(LoginRequestDto loginRequestDto, String userIpAddress) throws Exception {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));
        User user = userService.getByUsername(loginRequestDto.getUsername());

        loginController(user, userIpAddress);

        var jwt = jwtService.generateToken(user);
        user.setToken(jwt);
        user.setTokenExpiryDate(jwtService.extractExpiration(jwt));

        addIpAddress(user, userIpAddress);

        return  JwtAuthenticationResponseDto.builder().token(jwt).build();
    }

    private void checkIfEmailExists(String email) throws Exception {
        if(userService.existsByEmail(email)){
            throw new Exception("Email already exists");
        }
    }

    private boolean checkIfIpAddressNotExists(String ipAddress) {
        return !userService.existsIpAddressByIpAddress(ipAddress);
    }

    private void addIpAddress(User user, String userIpAddress) {
        if(checkIfIpAddressNotExists(userIpAddress)){
            IpAddress ipAddress = new IpAddress();
            ipAddress.setIpAddress(userIpAddress);
            IpAddress savedIpAddress = userService.saveIpAddress(ipAddress);
            user.getIpAddresses().add(savedIpAddress);
            userService.saveUser(user);
        }else {
            IpAddress ipAddress = userService.getIpAddressByIpAddress(userIpAddress);
            user.getIpAddresses().add(ipAddress);
            userService.saveUser(user);
        }
    }

    private void loginController(User user, String userIpAddress){
        if(userService.getIsBannedByIpAddress(userIpAddress)){
            throw new SecurityException("Ip address was banned");
        } else if (!user.isActive()) {
            throw new SecurityException("User is not active");
        }
    }
}