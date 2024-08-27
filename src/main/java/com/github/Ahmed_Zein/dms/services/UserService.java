package com.github.Ahmed_Zein.dms.security;

import com.github.Ahmed_Zein.dms.api.models.RegistrationBody;
import com.github.Ahmed_Zein.dms.exception.EmailExistsException;
import com.github.Ahmed_Zein.dms.models.LocalUser;
import com.github.Ahmed_Zein.dms.models.dto.LocalUserDTO;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final LocalUserDTO localUserDto;
    private final EncryptionService encryptionService;

    public UserService(LocalUserDTO localUserDto, EncryptionService encryptionService) {
        this.localUserDto = localUserDto;
        this.encryptionService = encryptionService;
    }

    public LocalUser register(RegistrationBody body) throws EmailExistsException {
        if (localUserDto.existsByEmailIgnoreCase(body.getEmail())) {
            throw new EmailExistsException();
        }
        body.setPassword(encryptionService.hashPassword(body.getPassword()));
        var user = LocalUser.from(body);
        return localUserDto.save(user);
    }
}
