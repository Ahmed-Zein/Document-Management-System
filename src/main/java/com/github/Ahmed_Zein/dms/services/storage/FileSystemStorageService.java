package com.github.Ahmed_Zein.dms.services.storage;

import com.github.Ahmed_Zein.dms.exception.StorageException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Service
public class FileSystemStorageService implements StorageService {
    String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads";

    @Override
    public File store(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            throw new StorageException("FILE_IS_EMPTY");
        }
        if (multipartFile.getOriginalFilename() == null) {
            throw new StorageException("FILE_NAME_IS_MISSING");
        }
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new StorageException("SERVER_ERR");
            }
        }
        File destinationFile = new File(directory, multipartFile.getOriginalFilename());
        multipartFile.transferTo(destinationFile);
        return destinationFile;
    }

    @Override
    public Path load(String filename) {
        return null;
    }
}
