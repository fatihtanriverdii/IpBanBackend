package fatyidha.io.ipbanapp.service.security.impl;

import fatyidha.io.ipbanapp.dto.request.LoginRequestDto;
import fatyidha.io.ipbanapp.dto.request.RegisterRequestDto;
import fatyidha.io.ipbanapp.dto.response.JwtAuthenticationResponseDto;
import fatyidha.io.ipbanapp.model.IpAddress;
import fatyidha.io.ipbanapp.model.User;
import fatyidha.io.ipbanapp.service.security.AuthenticationService;
import fatyidha.io.ipbanapp.service.security.IpAddressService;
import fatyidha.io.ipbanapp.service.security.JwtService;
import fatyidha.io.ipbanapp.service.security.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final IpAddressService ipAddressService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtAuthenticationResponseDto register(RegisterRequestDto registerRequestDto, String userIpAddress) throws Exception {
        checkIfEmailExists(registerRequestDto.getEmail());

        var user = User.builder().firstName(registerRequestDto.getFirstName()).lastName(registerRequestDto.getLastName()).username(registerRequestDto.getUsername())
                .email(registerRequestDto.getEmail()).password(passwordEncoder.encode(registerRequestDto.getPassword())).isActive(true).build();

        var jwt = jwtService.generateToken(user);
        user.setToken(jwt);
        user.setTokenExpiryDate(jwtService.extractExpiration(jwt));
        if(!checkIfIpAddressExists(userIpAddress)){
            addIpAddress(user, userIpAddress);
            return JwtAuthenticationResponseDto.builder().token(jwt).build();
        }
        IpAddress tempIpAdd = ipAddressService.getByIpAddress(userIpAddress);
        user.getIpAddresses().add(tempIpAdd);
        userService.saveUser(user);
        return JwtAuthenticationResponseDto.builder().token(jwt).build();
    }

    @Override
    public JwtAuthenticationResponseDto login(LoginRequestDto loginRequestDto, String userIpAddress) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));
        var user = userService.getUserByUsername(loginRequestDto.getUsername());
        if(ipAddressService.getIsBannedByIpAddress(userIpAddress)){
            return JwtAuthenticationResponseDto.builder().message("Ip address was banned").build();
        }
        if (!user.isActive()){
            return JwtAuthenticationResponseDto.builder().message("User is not active").build();
        }
        var jwt = jwtService.generateToken(user);
        user.setToken(jwt);
        user.setTokenExpiryDate(jwtService.extractExpiration(jwt));
        if(!checkIfIpAddressExists(userIpAddress)){
            addIpAddress(user, userIpAddress);
            return JwtAuthenticationResponseDto.builder().token(jwt).build();
        }
        userService.saveUser(user);
        return  JwtAuthenticationResponseDto.builder().token(jwt).build();
    }

    private void checkIfEmailExists(String email) throws Exception {
        if(userService.existsByEmail(email)){
            throw new Exception("Email already exists");
        }
    }

    private boolean checkIfIpAddressExists(String ipAddress) {
        return ipAddressService.existsByIpAddress(ipAddress);
    }

    private void addIpAddress(User user, String userIpAddress) {
        //var ipAddress = IpAddress.builder().ipAddress(userIpAddress).createdDate(new Date(System.currentTimeMillis())).build();

        IpAddress ipAddress = new IpAddress();
        ipAddress.setIpAddress(userIpAddress);
        IpAddress ip = ipAddressService.saveIpAddress(ipAddress);
        user.getIpAddresses().add(ip);
        userService.saveUser(user);
    }
}