package com.github.Ahmed_Zein.dms.api.controllers.workspace;

import com.github.Ahmed_Zein.dms.api.models.WorkspaceUpdate;
import com.github.Ahmed_Zein.dms.models.LocalUser;
import com.github.Ahmed_Zein.dms.services.PermissionService;
import com.github.Ahmed_Zein.dms.services.WorkSpaceService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/workspace")
@SecurityRequirement(name = "Bearer Authentication")
public class WorkspaceController {
    private final WorkSpaceService workSpaceService;
    private final PermissionService permissionService;

    public WorkspaceController(WorkSpaceService workSpaceService, PermissionService permissionService) {
        this.workSpaceService = workSpaceService;
        this.permissionService = permissionService;
    }

    @PatchMapping
    public ResponseEntity<?> updateWorkSpace(@AuthenticationPrincipal LocalUser user, @RequestBody WorkspaceUpdate updatedName, @PathVariable Long userId) {
        if (permissionService.hasNoPermission(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return null;
//        try {
////            return ResponseEntity.ok(workSpaceService.updateWorkSpace(userId, updatedName));
//            return null;
//        } catch (InvalidOperationException e) {
//            var map = new HashMap<String, String>();
//            map.put("message", e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
//        }

    }
//    TODO: delete workspace -> delete all directories

}
