package com.github.Ahmed_Zein.dms.models.dao;

import com.github.Ahmed_Zein.dms.models.Directory;
import com.github.Ahmed_Zein.dms.models.WorkSpace;
import org.springframework.data.repository.ListCrudRepository;

public interface WorkSpaceDAO extends ListCrudRepository<WorkSpace, Long> {
    boolean existsByDirectories(Directory directories);

    boolean existsByDirectoriesAndDirectories_WorkSpace(Directory directories, WorkSpace workSpace);
}
