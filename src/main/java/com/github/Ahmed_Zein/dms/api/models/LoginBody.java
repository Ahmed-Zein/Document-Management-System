package com.github.Ahmed_Zein.dms.api.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.repository.NoRepositoryBean;

@Data
@Builder
@AllArgsConstructor
@NoRepositoryBean
public class LoginBody {
    @Email
    private String email;

    @NotNull
    @NotBlank
    private String password;
}
