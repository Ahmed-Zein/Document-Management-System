package com.github.Ahmed_Zein.dms.api.controllers.documents;

import com.github.Ahmed_Zein.dms.exception.DirectoryNotFoundException;
import com.github.Ahmed_Zein.dms.exception.DocumentNotFoundException;
import com.github.Ahmed_Zein.dms.exception.InvalidOperationException;
import com.github.Ahmed_Zein.dms.exception.UserNotFoundException;
import com.github.Ahmed_Zein.dms.models.Document;
import com.github.Ahmed_Zein.dms.models.LocalUser;
import com.github.Ahmed_Zein.dms.services.DocumentService;
import com.github.Ahmed_Zein.dms.services.PermissionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/users/{userId}/workspace/directories/documents")
@SecurityRequirement(name = "Bearer Authentication")
public class DocumentController {
    private final DocumentService documentService;
    private final PermissionService permissionService;

    public DocumentController(DocumentService documentService, PermissionService permissionService) {
        this.documentService = documentService;
        this.permissionService = permissionService;
    }

    @PostMapping(value = "/{dirId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Document> saveDocuments(
            @RequestParam("file") MultipartFile multipartFile,
            @AuthenticationPrincipal LocalUser user,
            @PathVariable Long dirId,
            @PathVariable Long userId) throws IOException, UserNotFoundException, DirectoryNotFoundException {
        if (permissionService.hasNoPermission(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        var document = documentService.storeDocument(multipartFile, userId, dirId);
        return ResponseEntity.ok().body(document);
    }

    //    TODO: download document
    @GetMapping("/{dirId}/download/{filename:.+}")
    public ResponseEntity<Resource> downloadDocuments(
            @AuthenticationPrincipal LocalUser user,
            @PathVariable Long userId,
            @PathVariable Long dirId,
            @PathVariable String filename
    ) throws IOException, UserNotFoundException, DocumentNotFoundException {
        if (permissionService.hasNoPermission(user, dirId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        var resource = documentService.loadDocumentAsStream(filename, userId, dirId);
        if (resource == null) {
            return ResponseEntity.notFound().build();
        }

        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }

    @DeleteMapping("/{dirId}/download/{filename:.+}")
    public ResponseEntity<Void> deleteDocument(
            @AuthenticationPrincipal LocalUser user,
            @PathVariable Long userId,
            @PathVariable Long dirId,
            @PathVariable String filename
    ) throws UserNotFoundException, DocumentNotFoundException, DirectoryNotFoundException {
        if (permissionService.hasNoPermission(user, dirId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        documentService.deleteDocument(filename, userId, dirId);
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

    @ExceptionHandler(DocumentNotFoundException.class)
    public ResponseEntity<Map<String, String>> handelDocumentNotFound(Exception e) {
        Map<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

}
