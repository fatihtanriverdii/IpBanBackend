package fatyidha.io.ipbanapp.service.security;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public interface JwtService {

    String generateToken(UserDetails userDetails);
    String extractUsername(String token);
    Date extractExpiration(String token);
    boolean isTokenValid(String token, UserDetails userDetails);
}