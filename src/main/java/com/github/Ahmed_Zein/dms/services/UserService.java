package com.github.Ahmed_Zein.dms.services;

import com.github.Ahmed_Zein.dms.api.models.LoginBody;
import com.github.Ahmed_Zein.dms.api.models.RegistrationBody;
import com.github.Ahmed_Zein.dms.exception.EmailExistsException;
import com.github.Ahmed_Zein.dms.models.LocalUser;
import com.github.Ahmed_Zein.dms.models.UserRole;
import com.github.Ahmed_Zein.dms.models.dao.LocalUserDAO;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final JWTService jwtService;
    private final LocalUserDAO localUserDAO;
    private final EncryptionService encryptionService;
    private final static String WORKSPACE_DEFAULT_NAME = "My Workspace";

    public UserService(JWTService jwtService, LocalUserDAO localUserDAO, EncryptionService encryptionService) {
        this.jwtService = jwtService;
        this.localUserDAO = localUserDAO;
        this.encryptionService = encryptionService;
    }

    public LocalUser register(RegistrationBody body) throws EmailExistsException {
        if (localUserDAO.existsByEmailIgnoreCase(body.getEmail())) {
            throw new EmailExistsException();
        }
        var user = LocalUser.builder()
                .email(body.getEmail())
                .firstName(body.getFirstname())
                .lastName(body.getLastname())
                .password(encryptionService.hashPassword(body.getPassword()))
                .role(UserRole.USER)
                .workspaceName(WORKSPACE_DEFAULT_NAME);

        return localUserDAO.save(user.build());
    }

    public String login(LoginBody body) {
        var opUser = localUserDAO.findByEmailIgnoreCase(body.getEmail());
        if (opUser.isEmpty()) {
            return null;
        }
        var user = opUser.get();
        if (!encryptionService.verifyPassword(body.getPassword(), user.getPassword())) {
            return null;
        }
        return jwtService.generateToken(user);
    }
}
