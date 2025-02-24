package fatyidha.io.ipbanapp.controller;

import fatyidha.io.ipbanapp.model.User;
import fatyidha.io.ipbanapp.service.security.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/api/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/all-users")
    public ResponseEntity<List<User>> getAllUsers() throws Exception {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/status/{username}")
    public ResponseEntity<Boolean> banState(@PathVariable String username, HttpServletRequest request) throws Exception {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if(ipAddress == null || ipAddress.isEmpty()){
            ipAddress = request.getRemoteAddr();
        }
        System.out.println(userService.isBan(username, ipAddress));
        return ResponseEntity.ok(userService.isBan(username, ipAddress));
    }

    @PutMapping("/set-active/false/{username}")
    public ResponseEntity<String> setActiveFalse(@PathVariable String username) throws Exception {
        return ResponseEntity.ok(userService.isActiveFalse(username));
    }

    @PutMapping("/set-active/true/{username}")
    public ResponseEntity<String> setActiveTrue(@PathVariable String username) throws Exception {
        return ResponseEntity.ok(userService.isActiveTrue(username));
    }
}
