package com.github.Ahmed_Zein.dms.models.dto;

import com.github.Ahmed_Zein.dms.models.LocalUser;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface LocalUserDTO extends ListCrudRepository<LocalUser, Long> {
    boolean existsByEmailIgnoreCase(String email);

    Optional<LocalUser> findByEmailIgnoreCase(String email);
}
