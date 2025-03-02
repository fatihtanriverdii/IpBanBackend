package fatyidha.io.ipbanapp.service.security;

import fatyidha.io.ipbanapp.dto.request.LoginRequestDto;
import fatyidha.io.ipbanapp.dto.request.RegisterRequestDto;
import fatyidha.io.ipbanapp.dto.response.JwtAuthenticationResponseDto;

public interface AuthenticationService {
    JwtAuthenticationResponseDto register(RegisterRequestDto registerRequestDto, String userIpAddress) throws Exception;
    JwtAuthenticationResponseDto login(LoginRequestDto loginRequestDto, String userIpAddress) throws Exception;
}
