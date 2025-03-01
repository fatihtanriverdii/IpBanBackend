package fatyidha.io.ipbanapp.controller;

import fatyidha.io.ipbanapp.model.User;
import fatyidha.io.ipbanapp.service.security.UserService;
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
}