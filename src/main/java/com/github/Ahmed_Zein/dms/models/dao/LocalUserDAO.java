package com.github.Ahmed_Zein.dms.models.dao;

import com.github.Ahmed_Zein.dms.models.LocalUser;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface LocalUserDAO extends ListCrudRepository<LocalUser, Long> {
    boolean existsByEmailIgnoreCase(String email);

    Optional<LocalUser> findByEmailIgnoreCase(String email);

    @Override
    boolean existsById(Long userId);
}
