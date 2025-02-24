package fatyidha.io.ipbanapp.service.security.impl;

import fatyidha.io.ipbanapp.model.User;
import fatyidha.io.ipbanapp.repository.UserRepository;
import fatyidha.io.ipbanapp.service.security.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDetailsService getUserDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

    @Override
    public User getUserByEmail(String email) throws Exception{
        return findByEmail(email);
    }

    public String isActiveFalse(String username) throws Exception {
        User user = findByUsername(username);
        if(user != null){
            user.setActive(false);
            userRepository.saveAndFlush(user);
            return "isActive set to false";
        }
        return "user not found";
    }

    public String isActiveTrue(String username) throws Exception {
        User user = findByUsername(username);
        if(user != null){
            user.setActive(true);
            userRepository.saveAndFlush(user);
            return "isActive set to false";
        }
        return "user not found";
    }

    public List<User> getUsers() throws Exception {
        return userRepository.findAll();
    }

    public boolean isBan(String username, String userIpAddress) throws Exception {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new Exception("User not found"));
        if(user.getIpAddress().equals(userIpAddress) && !user.isActive()){
            return true;
        }
        return false;
    }

    private User findByEmail(String email) throws Exception {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("User not found"));
    }

    private User findByUsername(String username) throws Exception {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new Exception("User not found"));
    }
}
