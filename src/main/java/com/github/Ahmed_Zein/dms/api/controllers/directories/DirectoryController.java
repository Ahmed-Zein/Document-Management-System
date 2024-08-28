package com.github.Ahmed_Zein.dms.api.controllers.directories;

import com.github.Ahmed_Zein.dms.exception.DirectoryNotFoundException;
import com.github.Ahmed_Zein.dms.exception.InvalidOperationException;
import com.github.Ahmed_Zein.dms.exception.UserNotFoundException;
import com.github.Ahmed_Zein.dms.models.Directory;
import com.github.Ahmed_Zein.dms.models.LocalUser;
import com.github.Ahmed_Zein.dms.services.DirectoryService;
import com.github.Ahmed_Zein.dms.services.PermissionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    public ResponseEntity<?> getDirectories(@AuthenticationPrincipal LocalUser user, @PathVariable Long userId) {
        if (permissionService.hasNoPermission(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            return ResponseEntity.ok(directoryService.getDirectories(userId));
        } catch (UserNotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<?> addDirectory(@AuthenticationPrincipal LocalUser user, @RequestBody Directory directory, @PathVariable Long userId) {
        if (permissionService.hasNoPermission(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            return ResponseEntity.ok(directoryService.addDirectory(userId, directory));
        } catch (UserNotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/{dirId}")
    public ResponseEntity<?> getDirectory(@AuthenticationPrincipal LocalUser user, @PathVariable Long userId, @PathVariable Long dirId) {
        if (permissionService.hasNoPermission(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            return ResponseEntity.ok(directoryService.getDirectory(userId, dirId));
        } catch (DirectoryNotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Directory not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PatchMapping("/{dirId}")
    public ResponseEntity<?> updateDirectory(@PathVariable Long userId, @PathVariable Long dirId, @RequestBody Directory directory) {
        try {
            Directory updatedDirectory = directoryService.updateDirectory(userId, dirId, directory);
            return ResponseEntity.ok(updatedDirectory);
        } catch (UserNotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (DirectoryNotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Directory not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (InvalidOperationException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{dirId}")
    public ResponseEntity<?> deleteDirectory(@AuthenticationPrincipal LocalUser user, @PathVariable Long dirId, @PathVariable Long userId) {
        if (permissionService.hasNoPermission(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            directoryService.deleteDirectory(user, dirId);
            return ResponseEntity.ok().body(null);
        } catch (DirectoryNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
