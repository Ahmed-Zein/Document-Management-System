package com.github.Ahmed_Zein.dms.services.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public interface StorageService {
    File store(MultipartFile file) throws IOException;

     Path load(String filename);
}
