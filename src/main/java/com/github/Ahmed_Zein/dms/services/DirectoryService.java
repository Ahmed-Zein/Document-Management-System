package com.github.Ahmed_Zein.dms.services;

import com.github.Ahmed_Zein.dms.api.models.AddDirectoryBody;
import com.github.Ahmed_Zein.dms.exception.DirectoryNotFoundException;
import com.github.Ahmed_Zein.dms.exception.InvalidOperationException;
import com.github.Ahmed_Zein.dms.exception.UserNotFoundException;
import com.github.Ahmed_Zein.dms.models.Directory;
import com.github.Ahmed_Zein.dms.models.dao.DirectoryDAO;
import com.github.Ahmed_Zein.dms.models.dao.LocalUserDAO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        if (localUserDAO.existsById(userId)) {
            return directoryDAO.findByLocalUser_Id(userId);
        }
        throw new UserNotFoundException();
    }

    public Directory addDirectory(Long userId, AddDirectoryBody body) throws UserNotFoundException {
        var user = localUserDAO.findById(userId).orElseThrow(UserNotFoundException::new);
        var directory = Directory.builder()
                .id(0L)
                .name(body.getName())
                .isPublic(body.getIsPublic())
                .localUser(user)
                .createdAt(LocalDateTime.now())
                .build();
        return directoryDAO.save(directory);
    }

    public Directory getDirectory(Long userId, Long dirId) throws DirectoryNotFoundException, UserNotFoundException {
        var user = localUserDAO.findById(userId).orElseThrow(UserNotFoundException::new);
        return directoryDAO.findByIdAndLocalUser_Id(dirId, userId).orElseThrow(DirectoryNotFoundException::new);
    }

    public Directory updateDirectory(Long userId, Long dirId, AddDirectoryBody body)
            throws UserNotFoundException, DirectoryNotFoundException, InvalidOperationException {
        var user = localUserDAO.findById(userId).orElseThrow(UserNotFoundException::new);
        var directory = directoryDAO.findByIdAndLocalUser_Id(dirId, userId).orElseThrow(DirectoryNotFoundException::new);
        if (body.getName() != null ) {
            directory.setName(body.getName());
        }
        if (body.getIsPublic() != null) {
            directory.setIsPublic(body.getIsPublic());
        }
        return directoryDAO.save(directory);
    }

    public void deleteDirectory(Long userId, Long dirId) throws DirectoryNotFoundException, UserNotFoundException {
        var user = localUserDAO.findById(userId).orElseThrow(UserNotFoundException::new);
        var count = directoryDAO.deleteByIdAndLocalUser(dirId, user);
        if (count == 0) {
            throw new DirectoryNotFoundException();
        }
    }
}
