package com.github.Ahmed_Zein.dms.services;

import com.github.Ahmed_Zein.dms.models.Directory;
import com.github.Ahmed_Zein.dms.models.LocalUser;
import com.github.Ahmed_Zein.dms.models.dao.DirectoryDAO;
import com.github.Ahmed_Zein.dms.models.dao.WorkSpaceDAO;
import org.springframework.stereotype.Service;

@Service
public class WorkSpaceService {
    final private WorkSpaceDAO workSpaceDAO;
    private final DirectoryDAO directoryDAO;

    public WorkSpaceService(WorkSpaceDAO workSpaceDAO,
                            DirectoryDAO directoryDAO) {
        this.workSpaceDAO = workSpaceDAO;
        this.directoryDAO = directoryDAO;
    }

    public Directory addDirectory(LocalUser user, Directory directory) {
        directory.setId(0L);
        var userWorkSpace = user.getWorkSpace();
        directory.setWorkSpace(userWorkSpace);
        return directoryDAO.save(directory);
    }
}
