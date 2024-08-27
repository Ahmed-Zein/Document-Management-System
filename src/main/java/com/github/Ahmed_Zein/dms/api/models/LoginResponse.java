package com.github.Ahmed_Zein.dms.api.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String jwt;
    private boolean success;
    private String error;
}
