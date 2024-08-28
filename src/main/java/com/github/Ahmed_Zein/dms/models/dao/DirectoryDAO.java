package com.github.Ahmed_Zein.dms.models.dao;

import com.github.Ahmed_Zein.dms.models.Directory;
import org.springframework.data.repository.ListCrudRepository;

public interface DirectoryDAO extends ListCrudRepository<Directory, Long> {
//    List<Directory> findByWorkSpace(WorkSpace workSpace);
//
//    boolean existsByIdAndWorkSpace(Long id, WorkSpace workSpace);
//
//    Optional<Directory> findByIdAndWorkSpace_LocalUser_Id(Long id, Long id1);
//
//    Optional<Directory> findByIdAndWorkSpace(Long id, WorkSpace workSpace);
}
