package com.github.Ahmed_Zein.dms.services;

import com.github.Ahmed_Zein.dms.exception.DirectoryNotFoundException;
import com.github.Ahmed_Zein.dms.exception.DocumentNotFoundException;
import com.github.Ahmed_Zein.dms.exception.UserNotFoundException;
import com.github.Ahmed_Zein.dms.models.Document;
import com.github.Ahmed_Zein.dms.models.dao.DirectoryDAO;
import com.github.Ahmed_Zein.dms.models.dao.DocumentDAO;
import com.github.Ahmed_Zein.dms.models.dao.LocalUserDAO;
import com.github.Ahmed_Zein.dms.services.storage.FileSystemStorageService;
import jakarta.transaction.Transactional;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service
public class DocumentService {
    private final FileSystemStorageService storageService;
    private final LocalUserDAO localUserDAO;
    private final DirectoryDAO directoryDAO;
    private final DocumentDAO documentDAO;

    public DocumentService(FileSystemStorageService storageService,
                           LocalUserDAO localUserDAO,
                           DirectoryDAO directoryDAO,
                           DocumentDAO documentDAO) {
        this.storageService = storageService;
        this.localUserDAO = localUserDAO;
        this.directoryDAO = directoryDAO;
        this.documentDAO = documentDAO;
    }

    @Transactional
    public Document storeDocument(MultipartFile multipartFile, Long userId, Long dirId) throws UserNotFoundException, DirectoryNotFoundException, IOException {
        var owner = localUserDAO.findById(userId).orElseThrow(UserNotFoundException::new);
        var directory = directoryDAO.findByIdAndLocalUser_Id(dirId, userId).orElseThrow(DirectoryNotFoundException::new);
        var filename = multipartFile.getOriginalFilename();
        if (documentDAO.existsByName(filename)) {
            throw new FileAlreadyExistsException("file: " + filename + " Already Exists");
        }
        var parentPath = "/" + userId + "/" + dirId;
        storageService.store(multipartFile, parentPath);
        var document = Document.builder()
                .name(filename)
                .url(parentPath + "/" + filename)
                .createdAt(LocalDateTime.now())
                .contentType(multipartFile.getContentType())
                .directory(directory)
                .build();
        return documentDAO.save(document);
    }


    private final Path fileStorageLocation = Paths.get("/home/ava/Development/spring/dms/uploads");

    public FileSystemResource loadDocumentAsStream(String filename, Long userId, Long dirId) throws MalformedURLException, UserNotFoundException, DocumentNotFoundException {
        var owner = localUserDAO.findById(userId).orElseThrow(UserNotFoundException::new);
        if (!documentDAO.existsByNameAndDirectory_IdAndDirectory_LocalUser_Id(filename, dirId, userId)) {
            throw new DocumentNotFoundException("Document: " + filename + "doesn't exits for user ID: " + userId);
        }
        try {
            Path filePath = fileStorageLocation.resolve(fileStorageLocation + "/" + userId + "/" + dirId + "/" + filename).normalize();

            if (Files.exists(filePath)) {
                return new FileSystemResource(filePath.toFile());
            } else {
                throw new FileNotFoundException("File not found: " + filename);
            }
        } catch (IOException e) {
            throw new MalformedURLException("Could not read file: " + filename);
        }
    }

    public void deleteDocument(String filename, Long userId, Long dirId) throws UserNotFoundException, DocumentNotFoundException, DirectoryNotFoundException {
        var owner = localUserDAO.findById(userId).orElseThrow(UserNotFoundException::new);
        var directory = directoryDAO.findByIdAndLocalUser_Id(dirId, userId).orElseThrow(DirectoryNotFoundException::new);
        documentDAO.deleteByNameAndDirectory(filename, directory);
    }

}
