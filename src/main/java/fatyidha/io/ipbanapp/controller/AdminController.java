package fatyidha.io.ipbanapp.controller;

import fatyidha.io.ipbanapp.dto.request.IpAddressRequestDto;
import fatyidha.io.ipbanapp.dto.request.UsernameRequestDto;
import fatyidha.io.ipbanapp.model.User;
import fatyidha.io.ipbanapp.service.security.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/api/admin")
public class AdminController {
    private final UserService userService;

    @PutMapping("/ban/user")
    public ResponseEntity<String> banUser(@RequestBody UsernameRequestDto usernameRequestDto) {
        return ResponseEntity.ok(userService.banUser(usernameRequestDto.getUsername()));
    }

    @PutMapping("/unBan/user")
    public ResponseEntity<String> unbanUser(@RequestBody UsernameRequestDto usernameRequestDto) {
        return ResponseEntity.ok(userService.unBanUser(usernameRequestDto.getUsername()));
    }

    @PutMapping("/ban/ipAddress")
    public ResponseEntity<String> banUserIpaddress(@RequestBody IpAddressRequestDto ipAddressRequestDto) {
        return ResponseEntity.ok(userService.banIpAddress(ipAddressRequestDto.getIpAddress()));
    }

    @PutMapping("/unBan/ipAddress")
    public ResponseEntity<String> unBanUserIpAddress(@RequestBody IpAddressRequestDto ipAddressRequestDto) {
        return ResponseEntity.ok(userService.unBanIpAddress(ipAddressRequestDto.getIpAddress()));
    }

    @GetMapping("/all-users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }
}
