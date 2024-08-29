package com.github.Ahmed_Zein.dms.api.controllers.workspace;

import com.github.Ahmed_Zein.dms.api.models.WorkspaceUpdate;
import com.github.Ahmed_Zein.dms.exception.UserNotFoundException;
import com.github.Ahmed_Zein.dms.models.LocalUser;
import com.github.Ahmed_Zein.dms.services.PermissionService;
import com.github.Ahmed_Zein.dms.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users/{userId}/workspace")
@SecurityRequirement(name = "Bearer Authentication")
public class WorkspaceController {
    private final PermissionService permissionService;
    private final UserService userService;

    public WorkspaceController(PermissionService permissionService, UserService userService) {
        this.permissionService = permissionService;
        this.userService = userService;
    }

    @PatchMapping
    public ResponseEntity<?> updateWorkSpace(@AuthenticationPrincipal LocalUser user, @RequestBody WorkspaceUpdate updatedName, @PathVariable Long userId) {
        if (permissionService.hasNoPermission(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            return ResponseEntity.ok(userService.updateWorkspaceName(userId, updatedName));
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
//    TODO: delete workspace -> delete all directories

}
