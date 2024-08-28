package com.github.Ahmed_Zein.dms.services;

import com.github.Ahmed_Zein.dms.api.models.WorkspaceUpdate;
import com.github.Ahmed_Zein.dms.exception.InvalidOperationException;
import com.github.Ahmed_Zein.dms.models.WorkSpace;
import com.github.Ahmed_Zein.dms.models.dao.WorkSpaceDAO;
import org.springframework.stereotype.Service;

@Service
public class WorkSpaceService {

    private final WorkSpaceDAO workSpaceDAO;

    public WorkSpaceService(WorkSpaceDAO workSpaceDAO) {
        this.workSpaceDAO = workSpaceDAO;
    }

    public WorkSpace updateWorkSpace(Long userId, WorkspaceUpdate updatedName) throws InvalidOperationException {
        var userWorkSpace = workSpaceDAO.findByLocalUser_Id(userId).orElseThrow(() -> new InvalidOperationException("Wrong user ID - " + userId));
        userWorkSpace.setName(updatedName.getName());
        return workSpaceDAO.save(userWorkSpace);
    }
}
