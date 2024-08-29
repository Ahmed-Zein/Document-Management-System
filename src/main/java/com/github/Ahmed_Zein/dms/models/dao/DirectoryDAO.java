package com.github.Ahmed_Zein.dms.models.dao;

import com.github.Ahmed_Zein.dms.models.Directory;
import com.github.Ahmed_Zein.dms.models.LocalUser;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface DirectoryDAO extends ListCrudRepository<Directory, Long> {
    List<Directory> findByLocalUser_Id(Long id);

    Optional<Directory> findByIdAndLocalUser_Id(Long id, Long id1);

    long deleteByIdAndLocalUser(Long id, LocalUser localUser);
}
