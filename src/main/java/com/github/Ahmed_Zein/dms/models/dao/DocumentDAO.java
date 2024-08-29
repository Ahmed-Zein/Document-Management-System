package com.github.Ahmed_Zein.dms.models.dao;

import com.github.Ahmed_Zein.dms.models.Directory;
import com.github.Ahmed_Zein.dms.models.Document;
import org.springframework.data.repository.ListCrudRepository;

public interface DocumentDAO extends ListCrudRepository<Document, Long> {
    boolean existsByName(String name);

    boolean existsByNameAndDirectory_IdAndDirectory_LocalUser_Id(String name, Long id, Long id1);

    long deleteByNameAndDirectory(String name, Directory directory);
}
