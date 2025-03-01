package fatyidha.io.ipbanapp.service.security;

import fatyidha.io.ipbanapp.model.IpAddress;

public interface IpAddressService {
    boolean getIsBannedByIpAddress(String ipAddress);
    boolean existsByIpAddress(String ipAddress);
    void saveIpAddress(IpAddress ipAddress);
}
