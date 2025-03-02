package fatyidha.io.ipbanapp.service.security.impl;

import fatyidha.io.ipbanapp.model.IpAddress;
import fatyidha.io.ipbanapp.repository.IpAddressRepository;
import fatyidha.io.ipbanapp.service.security.IpAddressService;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IpAddressServiceImpl implements IpAddressService {
    private final IpAddressRepository ipAddressRepository;

    @Override
    public boolean getIsBannedByIpAddress(String ipAddress) {
        return ipAddressRepository.findByIpAddress(ipAddress)
                .map(IpAddress :: isBanned)
                .orElse(false);
    }

    @Override
    public IpAddress getByIpAddress(String userIpAddress) {
        return ipAddressRepository.findByIpAddress(userIpAddress)
                .orElseThrow(() -> new RuntimeException("ip address not found"));
    }

    @Override
    public boolean existsByIpAddress(String ipAddress) {
        return ipAddressRepository.existsByIpAddress(ipAddress);
    }

    @Override
    public IpAddress saveIpAddress(IpAddress ipAddress) {
        return ipAddressRepository.save(ipAddress);
    }
}
