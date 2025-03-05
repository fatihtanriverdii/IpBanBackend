package fatyidha.io.ipbanapp.controller;

import fatyidha.io.ipbanapp.dto.request.IpAddressRequestDto;
import fatyidha.io.ipbanapp.dto.request.UsernameRequestDto;
import fatyidha.io.ipbanapp.model.IpAddress;
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

    @PostMapping("/add/ipAddress")
    public ResponseEntity<IpAddress> addIpAddress(@RequestBody IpAddressRequestDto ipAddressRequestDto){
        IpAddress ipAddress = new IpAddress();
        ipAddress.setIpAddress(ipAddressRequestDto.getIpAddress());
        return ResponseEntity.ok(userService.saveIpAddress(ipAddress));
    }
}