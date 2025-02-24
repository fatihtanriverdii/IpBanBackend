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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/auth")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<JwtAuthenticationResponseDto> register(@RequestBody RegisterRequestDto registerRequestDto, HttpServletResponse response, HttpServletRequest request) throws Exception {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if(ipAddress == null || ipAddress.isEmpty()){
            ipAddress = request.getRemoteAddr();
        }

        String token = authenticationService.register(registerRequestDto, ipAddress).getToken();

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(3600);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok(new JwtAuthenticationResponseDto(token, null));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponseDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response, HttpServletRequest request) throws Exception {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if(ipAddress == null || ipAddress.isEmpty()){
            ipAddress = request.getRemoteAddr();
        }

        boolean isBanState = userService.isBan(loginRequestDto.getUsername(), ipAddress);

        if(!isBanState){
            String token = authenticationService.login(loginRequestDto).getToken();

            Cookie cookie = new Cookie("token", token);
            //cookie.setHttpOnly(true);
            cookie.setMaxAge(3600);
            cookie.setPath("/");
            response.addCookie(cookie);

            return ResponseEntity.ok(new JwtAuthenticationResponseDto(token, null));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JwtAuthenticationResponseDto(null,"Kullanıcı banlı"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) throws Exception {
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
