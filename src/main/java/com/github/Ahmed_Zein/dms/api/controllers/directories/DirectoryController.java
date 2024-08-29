package com.github.Ahmed_Zein.dms.api.controllers.directories;

import com.github.Ahmed_Zein.dms.api.models.AddDirectoryBody;
import com.github.Ahmed_Zein.dms.exception.DirectoryNotFoundException;
import com.github.Ahmed_Zein.dms.exception.InvalidOperationException;
import com.github.Ahmed_Zein.dms.exception.UserNotFoundException;
import com.github.Ahmed_Zein.dms.models.Directory;
import com.github.Ahmed_Zein.dms.models.LocalUser;
import com.github.Ahmed_Zein.dms.services.DirectoryService;
import com.github.Ahmed_Zein.dms.services.PermissionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users/{userId}/workspace/directories")
@SecurityRequirement(name = "Bearer Authentication")
public class DirectoryController {

    private final DirectoryService directoryService;
    private final PermissionService permissionService;

    public DirectoryController(DirectoryService directoryService, PermissionService permissionService) {
        this.directoryService = directoryService;
        this.permissionService = permissionService;
    }

    @GetMapping
    public ResponseEntity<List<Directory>> getDirectories(@AuthenticationPrincipal LocalUser user, @PathVariable Long userId) throws UserNotFoundException {
        if (permissionService.hasNoPermission(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(directoryService.getDirectories(userId));
    }

    @PostMapping
    public ResponseEntity<Directory> addDirectory(@AuthenticationPrincipal LocalUser user, @Valid @RequestBody AddDirectoryBody body, @PathVariable Long userId) throws UserNotFoundException {
        if (permissionService.hasNoPermission(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(directoryService.addDirectory(userId, body));
    }

    @GetMapping("/{dirId}")
    public ResponseEntity<Directory> getDirectory(@AuthenticationPrincipal LocalUser user, @PathVariable Long userId, @PathVariable Long dirId) throws DirectoryNotFoundException, UserNotFoundException {
        if (permissionService.hasNoPermission(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(directoryService.getDirectory(userId, dirId));
    }

    @PatchMapping("/{dirId}")
    public ResponseEntity<?> updateDirectory(@AuthenticationPrincipal LocalUser user, @PathVariable Long userId, @PathVariable Long dirId, @Valid @RequestBody AddDirectoryBody body) throws UserNotFoundException, DirectoryNotFoundException, InvalidOperationException {
        if (permissionService.hasNoPermission(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Directory updatedDirectory = directoryService.updateDirectory(userId, dirId, body);
        return ResponseEntity.ok(updatedDirectory);
    }

    @DeleteMapping("/{dirId}")
    public ResponseEntity<Void> deleteDirectory(@AuthenticationPrincipal LocalUser user, @PathVariable Long dirId, @PathVariable Long userId) throws DirectoryNotFoundException, UserNotFoundException {
        if (permissionService.hasNoPermission(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        directoryService.deleteDirectory(userId, dirId);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(DirectoryNotFoundException.class)
    public ResponseEntity<Map<String, String>> directoryNotFoundHandler() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Directory not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handelUserNotFound() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "User not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<Map<String, String>> handlerInvalidOperation(Exception e) {
        Map<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
