package fatyidha.io.ipbanapp.service.security;

import fatyidha.io.ipbanapp.model.IpAddress;

public interface IpAddressService {
    boolean getIsBannedByIpAddress(String ipAddress);
    IpAddress getByIpAddress(String userIpAddress);
    boolean existsByIpAddress(String ipAddress);
    IpAddress saveIpAddress(IpAddress ipAddress);
}
