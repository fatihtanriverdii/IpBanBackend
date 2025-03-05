package fatyidha.io.ipbanapp.service.security;

import fatyidha.io.ipbanapp.model.IpAddress;
import fatyidha.io.ipbanapp.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {
    UserDetailsService getUserDetailsService();
    User getUserByEmail(String email) throws Exception;
    String setExpirationEnd(String username) throws Exception;
    List<User> getUsers();
    void saveUser(User user);
    IpAddress saveIpAddress(IpAddress ipAddress);
    IpAddress getIpAddressByIpAddress(String ipAddress);
    User getByUsername(String username) throws Exception;
    String banUser(String username);
    String unBanUser(String username);
    String banIpAddress(String userIpAddress);
    String unBanIpAddress(String userIpAddress);
    boolean existsByEmail(String email);
    boolean banState(String username, String userIpAddress);
    boolean existsIpAddressByIpAddress(String ipAddress);
    boolean getIsBannedByIpAddress(String ipAddress);
}
