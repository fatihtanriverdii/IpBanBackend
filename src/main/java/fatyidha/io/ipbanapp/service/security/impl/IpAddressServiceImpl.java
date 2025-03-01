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
        return ipAddressRepository.findIsBannedByIpAddress(ipAddress);
    }

    public boolean existsByIpAddress(String ipAddress) {
        return ipAddressRepository.existsByIpAddress(ipAddress);
    }

    public void saveIpAddress(IpAddress ipAddress) {
        ipAddressRepository.save(ipAddress);
    }
}
