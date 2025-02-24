package fatyidha.io.ipbanapp.repository;

import fatyidha.io.ipbanapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(@Param("email") String email);
    Optional<User> findByUsername(@Param("username") String username);
    boolean existsByEmail(@Param("email") String email);
    @Query("SELECT u.ipAddress FROM User u WHERE u.username = :username")
    String findIpAddressByUsername(@Param("username") String username);
}
