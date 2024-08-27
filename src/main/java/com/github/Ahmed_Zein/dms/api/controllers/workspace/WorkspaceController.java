package com.github.Ahmed_Zein.dms.api.controllers.workspace;

import com.github.Ahmed_Zein.dms.models.Directory;
import com.github.Ahmed_Zein.dms.models.LocalUser;
import com.github.Ahmed_Zein.dms.models.UserRole;
import com.github.Ahmed_Zein.dms.models.dao.DirectoryDAO;
import com.github.Ahmed_Zein.dms.services.WorkSpaceService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/workspace")
@SecurityRequirement(name = "Bearer Authentication")
public class WorkspaceController {

    private final DirectoryDAO directoryDAO;
    private final WorkSpaceService workSpaceService;

    public WorkspaceController(DirectoryDAO directoryDAO,
                               WorkSpaceService workSpaceService) {
        this.directoryDAO = directoryDAO;
        this.workSpaceService = workSpaceService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Directory>> getDirectories(@AuthenticationPrincipal LocalUser user, @PathVariable Long userId) {
        if (hasNoPermission(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(directoryDAO.findByWorkSpace(user.getWorkSpace()));
    }

    @PostMapping("/")
    public ResponseEntity<Directory> addDirectory(@AuthenticationPrincipal LocalUser user, @RequestBody Directory directory, @PathVariable Long userId) {
        if (hasNoPermission(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        var dir = workSpaceService.addDirectory(user, directory);
        return ResponseEntity.ok(dir);
    }

    @GetMapping("/directory/{dirId}")
    public ResponseEntity<Directory> getWorkspace(@AuthenticationPrincipal LocalUser user, @PathVariable Long userId, @PathVariable Long dirId) {
        if (hasNoPermission(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        var opDirectory = directoryDAO.findById(dirId);

        return opDirectory.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    private boolean hasNoPermission(LocalUser user, Long userId) {
        return !(user.getId().equals(userId) || user.getRole().equals(UserRole.ADMIN));
    }
}
