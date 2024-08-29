package com.github.Ahmed_Zein.dms.api.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddDirectoryBody {
    @NotBlank
    private String name;
    private Boolean isPublic;
}
