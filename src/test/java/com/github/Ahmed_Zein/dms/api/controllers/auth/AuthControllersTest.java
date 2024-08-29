package com.github.Ahmed_Zein.dms.api.controllers.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.Ahmed_Zein.dms.api.models.LoginBody;
import com.github.Ahmed_Zein.dms.api.models.RegistrationBody;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllersTest {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @Transactional
    public void testRegistration() throws Exception {
        var mapper = new ObjectMapper();
        var registrationBody = RegistrationBody.builder()
                .email("testa@test.com")
                .firstname("FIRST_NAME")
                .lastname("LAST_NAME")
                .password("password123")
                .build();
        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(registrationBody)))
                .andExpect(status().isConflict());

        registrationBody.setEmail("testZ@test.com");
        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(registrationBody)))
                .andExpect(status().isOk());
    }

    @Test
    public void testLogin() throws Exception {
        var body = LoginBody.builder()
                .email("unvalidEmail@test.com")
                .password("WRONG_PASSWORD")
                .build();
        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isNotFound());

        body.setEmail("testa@test.com");
        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
        body.setPassword("passwordA123");
        ResultActions result = mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk());
        result.andExpect(jsonPath("$.jwt").exists())
                .andExpect(jsonPath("$.jwt").isNotEmpty());
    }
}
