package com.github.Ahmed_Zein.dms.services;

import com.github.Ahmed_Zein.dms.exception.DirectoryNotFoundException;
import com.github.Ahmed_Zein.dms.exception.InvalidOperationException;
import com.github.Ahmed_Zein.dms.exception.UserNotFoundException;
import com.github.Ahmed_Zein.dms.models.Directory;
import com.github.Ahmed_Zein.dms.models.LocalUser;
import com.github.Ahmed_Zein.dms.models.dao.DirectoryDAO;
import com.github.Ahmed_Zein.dms.models.dao.LocalUserDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DirectoryService {
    private final DirectoryDAO directoryDAO;
    private final LocalUserDAO localUserDAO;

    public DirectoryService(DirectoryDAO directoryDAO, LocalUserDAO localUserDAO) {
        this.directoryDAO = directoryDAO;
        this.localUserDAO = localUserDAO;
    }

    public List<Directory> getDirectories(Long userId) throws UserNotFoundException {
        var user = localUserDAO.findById(userId).orElseThrow(UserNotFoundException::new);
        return directoryDAO.findByWorkSpace(user.getWorkSpace());
    }

    public Directory addDirectory(Long userId, Directory directory) throws UserNotFoundException {
        directory.setId(0L);
        var user = localUserDAO.findById(userId).orElseThrow(UserNotFoundException::new);
        var userWorkSpace = user.getWorkSpace();
        directory.setWorkSpace(userWorkSpace);
        return directoryDAO.save(directory);
    }

    public Directory getDirectory(Long userId, Long dirId) throws DirectoryNotFoundException {
        return directoryDAO.findByIdAndWorkSpace_LocalUser_Id(dirId, userId).orElseThrow(DirectoryNotFoundException::new);
    }

    public Directory updateDirectory(Long userId, Long dirId, Directory directory)
            throws UserNotFoundException, DirectoryNotFoundException, InvalidOperationException {

        var user = localUserDAO.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found for ID: " + userId));

        if (directory == null || !dirId.equals(directory.getId())) {
            throw new InvalidOperationException("Directory ID mismatch or directory is null.");
        }

        Directory originalDir = directoryDAO.findById(dirId)
                .orElseThrow(() -> new DirectoryNotFoundException("Directory not found for ID: " + dirId));

        if (!user.getWorkSpace().equals(originalDir.getWorkSpace())) {
            throw new InvalidOperationException("User does not have permission to update this directory.");
        }

        directory.setWorkSpace(originalDir.getWorkSpace());
        return directoryDAO.save(directory);
    }

    public void deleteDirectory(LocalUser user, Long dirId) throws DirectoryNotFoundException {
        var userWorkSpace = user.getWorkSpace();
        if (directoryDAO.existsByIdAndWorkSpace(dirId, userWorkSpace)) {
            directoryDAO.deleteById(dirId);
        } else {
            throw new DirectoryNotFoundException();
        }
    }
}
