package com.github.Ahmed_Zein.dms.services.storage;

import com.github.Ahmed_Zein.dms.exception.StorageException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileSystemStorageService {
    String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads";

    public void store(MultipartFile multipartFile, String parentPath) throws IOException {
        if (multipartFile.isEmpty()) {
            throw new StorageException("FILE_IS_EMPTY");
        }
        if (multipartFile.getOriginalFilename() == null) {
            throw new StorageException("FILE_NAME_IS_MISSING");
        }
        File directory = new File(UPLOAD_DIR + parentPath);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new StorageException("SERVER_ERR");
            }
        }
        File destinationFile = new File(directory, multipartFile.getOriginalFilename());
        multipartFile.transferTo(destinationFile);
    }

}
