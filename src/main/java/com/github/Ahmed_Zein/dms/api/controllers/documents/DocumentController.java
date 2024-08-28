package com.github.Ahmed_Zein.dms.api.controllers.documents;

import com.github.Ahmed_Zein.dms.exception.DirectoryNotFoundException;
import com.github.Ahmed_Zein.dms.exception.UserNotFoundException;
import com.github.Ahmed_Zein.dms.models.Document;
import com.github.Ahmed_Zein.dms.models.LocalUser;
import com.github.Ahmed_Zein.dms.services.DocumentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/users/{userId}/workspace/directories/documents")
@SecurityRequirement(name = "Bearer Authentication")
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    //    TODO: get document
    @GetMapping()
    public void getDocuments() {

    }

    //    TODO: add document -> via upload
    @PostMapping(value = "/{dirId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Document> saveDocuments(
            @RequestParam("file") MultipartFile multipartFile,
            @AuthenticationPrincipal LocalUser user,
            @PathVariable Long dirId,
            @PathVariable Long userId) throws IOException {

        try {
            var document = documentService.storeDocument(multipartFile, userId, dirId);
            return ResponseEntity.ok().body(document);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (DirectoryNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

//    TODO: download document
//    TODO: Delete document
//    TODO: update document -> (name)
}
