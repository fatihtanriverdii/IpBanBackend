package fatyidha.io.ipbanapp.controller;

import fatyidha.io.ipbanapp.dto.request.LoginRequestDto;
import fatyidha.io.ipbanapp.dto.request.RegisterRequestDto;
import fatyidha.io.ipbanapp.dto.response.JwtAuthenticationResponseDto;
import fatyidha.io.ipbanapp.service.security.AuthenticationService;
import fatyidha.io.ipbanapp.service.security.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<JwtAuthenticationResponseDto> register(@RequestBody RegisterRequestDto registerRequestDto, HttpServletResponse response, HttpServletRequest request) throws Exception {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if(ipAddress == null || ipAddress.isEmpty()){
            ipAddress = request.getRemoteAddr();
        }

        var authResponse = authenticationService.register(registerRequestDto, ipAddress);
        String token = authResponse.getToken();

        if(token != null){
            Cookie cookie = new Cookie("token", token);
            cookie.setMaxAge(3600);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        return ResponseEntity.ok(new JwtAuthenticationResponseDto(token, authResponse.getMessage()));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponseDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response, HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if(ipAddress == null || ipAddress.isEmpty()){
            ipAddress = request.getRemoteAddr();
        }
        var authResponse = authenticationService.login(loginRequestDto, ipAddress);
        String token = authResponse.getToken();
        if(token != null){
            Cookie cookie = new Cookie("token", token);
            cookie.setMaxAge(3600);
            cookie.setPath("/");
            response.addCookie(cookie);
            return ResponseEntity.ok(new JwtAuthenticationResponseDto(token, authResponse.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JwtAuthenticationResponseDto(token, authResponse.getMessage()));
    }

    /*@PostMapping("/admin/login")
    public ResponseEntity<JwtAuthenticationResponseDto> adminLogin(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response, HttpServletRequest request) throws Exception {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        }
    }*/

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
