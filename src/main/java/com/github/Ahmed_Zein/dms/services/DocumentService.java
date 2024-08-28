package com.github.Ahmed_Zein.dms.services;

import com.github.Ahmed_Zein.dms.exception.DirectoryNotFoundException;
import com.github.Ahmed_Zein.dms.exception.UserNotFoundException;
import com.github.Ahmed_Zein.dms.models.Document;
import com.github.Ahmed_Zein.dms.models.dao.DirectoryDAO;
import com.github.Ahmed_Zein.dms.models.dao.DocumentDAO;
import com.github.Ahmed_Zein.dms.models.dao.LocalUserDAO;
import com.github.Ahmed_Zein.dms.services.storage.FileSystemStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    public Document storeDocument(MultipartFile multipartFile, Long userId, Long dirId) throws UserNotFoundException, DirectoryNotFoundException, IOException {
        var owner = localUserDAO.findById(userId).orElseThrow(UserNotFoundException::new);
        var directory = directoryDAO.findByIdAndWorkSpace(dirId, owner.getWorkSpace()).orElseThrow(DirectoryNotFoundException::new);
        var file = storageService.store(multipartFile);
        var document = Document.builder()
                .name(multipartFile.getOriginalFilename())
                .url(file.getPath())
                .directory(directory)
                .build();
        return documentDAO.save(document);
    }
}
