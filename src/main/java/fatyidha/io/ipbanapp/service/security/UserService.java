package fatyidha.io.ipbanapp.service.security;

import fatyidha.io.ipbanapp.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {
    UserDetailsService getUserDetailsService();
    User getUserByEmail(String email) throws Exception;
    String setExpirationEnd(String username) throws Exception;
    List<User> getUsers() throws Exception;
    void saveUser(User user);
    User getUserByUsername(String username);
    boolean existsByEmail(String email);
    boolean banState(String username, String userIpAddress);
}
