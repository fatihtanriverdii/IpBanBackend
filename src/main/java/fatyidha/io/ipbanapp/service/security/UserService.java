package fatyidha.io.ipbanapp.service.security;

import fatyidha.io.ipbanapp.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {
    UserDetailsService getUserDetailsService();
    User getUserByEmail(String email) throws Exception;
    String isActiveFalse(String username) throws Exception;
    String isActiveTrue(String username) throws Exception;
    List<User> getUsers() throws Exception;
    boolean isBan(String username, String userIpAddress) throws Exception;
}
