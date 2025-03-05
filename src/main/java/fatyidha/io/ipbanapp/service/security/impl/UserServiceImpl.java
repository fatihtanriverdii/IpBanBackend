package fatyidha.io.ipbanapp.service.security.impl;

import fatyidha.io.ipbanapp.model.IpAddress;
import fatyidha.io.ipbanapp.model.User;
import fatyidha.io.ipbanapp.repository.UserRepository;
import fatyidha.io.ipbanapp.service.security.IpAddressService;
import fatyidha.io.ipbanapp.service.security.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final IpAddressService ipAddressService;

    @Override
    public UserDetailsService getUserDetailsService() {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public User getUserByEmail(String email) throws Exception{
        return findByEmail(email);
    }

    @Override
    public String setExpirationEnd(String username) throws Exception {
        User user = getByUsername(username);
        if(user != null){
            user.setTokenExpiryDate(new Date(System.currentTimeMillis() - 60 * 1000));
            userRepository.saveAndFlush(user);
            return "tokenExpiryDate set to " + user.getTokenExpiryDate();
        }
        return "user not found";
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User getByUsername(String username) throws Exception {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new Exception("User not found"));
    }

    @Override
    public IpAddress saveIpAddress(IpAddress ipAddress){
        if(existsIpAddressByIpAddress(ipAddress.getIpAddress())){
            throw new IllegalArgumentException("IpAddress already exists");
        }
        return ipAddressService.saveIpAddress(ipAddress);
    }

    @Override
    public IpAddress getIpAddressByIpAddress(String ipAddress){
        return ipAddressService.getByIpAddress(ipAddress);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsIpAddressByIpAddress(String ipAddress) {
        return ipAddressService.existsByIpAddress(ipAddress);
    }

    @Override
    public String banUser(String username){
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if(user.isActive()){
            user.setActive(false);
            userRepository.saveAndFlush(user);
            return "user successfully banned";
        }else {
            return "user already banned";
        }
    }

    @Override
    public String unBanUser(String username){
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if(user.isActive()){
            return "user already unbanned";
        }else {
            user.setActive(true);
            userRepository.saveAndFlush(user);
            return "user successfully unbanned";
        }
    }

    @Override
    public String banIpAddress(String userIpAddress){
        if(ipAddressService.existsByIpAddress(userIpAddress)){
            IpAddress ipAddress = ipAddressService.getByIpAddress(userIpAddress);
            if(!ipAddress.isBanned()){
                ipAddress.setBanned(true);
                ipAddressService.saveIpAddress(ipAddress);
                return "ipAddress successfully banned";
            }else{
                return "ipAddress already banned";
            }
        }else{
            return "ipAddress not found";
        }
    }

    @Override
    public String unBanIpAddress(String userIpAddress){
        if(ipAddressService.existsByIpAddress(userIpAddress)){
            IpAddress ipAddress = ipAddressService.getByIpAddress(userIpAddress);
            if(!ipAddress.isBanned()){
                return "ipAddress already banned";
            }else{
                ipAddress.setBanned(false);
                ipAddressService.saveIpAddress(ipAddress);
                return "ipAddress successfully banned";
            }
        }else{
            return "ipAddress not found";
        }
    }

    @Override
    public boolean getIsBannedByIpAddress(String ipAddress){
        return ipAddressService.getIsBannedByIpAddress(ipAddress);
    }

    @Override
    public boolean banState(String username, String userIpAddress) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if(ipAddressService.getIsBannedByIpAddress(userIpAddress) || !user.isActive()){
            return true;
        }
        return false;
    }

    private User findByEmail(String email) throws Exception {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("User not found"));
    }
}
