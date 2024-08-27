package com.github.Ahmed_Zein.dms.services;

import com.github.Ahmed_Zein.dms.models.LocalUser;
import com.github.Ahmed_Zein.dms.models.UserRole;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {
    public boolean hasNoPermission(LocalUser user, Long userId) {
        return !(user.getId().equals(userId) || user.getRole().equals(UserRole.ADMIN));
    }
}
