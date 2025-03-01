package fatyidha.io.ipbanapp.service.security.impl;

import fatyidha.io.ipbanapp.model.IpAddress;
import fatyidha.io.ipbanapp.repository.IpAddressRepository;
import fatyidha.io.ipbanapp.service.security.IpAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IpAddressServiceImpl implements IpAddressService {
    private final IpAddressRepository ipAddressRepository;

    public boolean getIsBannedByIpAddress(String ipAddress) {
        IpAddress userIpAddress = ipAddressRepository.findByIpAddress(ipAddress)
                .orElseThrow(() -> new RuntimeException("ip address not found"));
        return userIpAddress.isBanned();
    }

    @Override
    public IpAddress getByIpAddress(String ipAddress) {
        return ipAddressRepository.findByIpAddress(ipAddress)
                .orElseThrow(() -> new RuntimeException("ip address not found"));
    }

    public boolean existsByIpAddress(String ipAddress) {
        return ipAddressRepository.existsByIpAddress(ipAddress);
    }

    public IpAddress saveIpAddress(IpAddress ipAddress) {
        return ipAddressRepository.save(ipAddress);
    }
}
