package fatyidha.io.ipbanapp.repository;

import fatyidha.io.ipbanapp.model.IpAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IpAddressRepository extends JpaRepository<IpAddress, Long> {
    boolean existsByIpAddress(@Param("ipAddress") String ipAddress);
    @Query("SELECT i.isBanned FROM IpAddress i WHERE i.ipAddress = :ipAddress")
    boolean findIsBannedByIpAddress(@Param("ipAddress") String ipAddress);
}
