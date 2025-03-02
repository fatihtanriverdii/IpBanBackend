package fatyidha.io.ipbanapp.service.security.impl;

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
    public List<User> getUsers() throws Exception {
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
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /*public String banIp(String username){
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


    }*/

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
