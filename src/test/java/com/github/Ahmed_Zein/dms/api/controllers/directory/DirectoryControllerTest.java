package com.github.Ahmed_Zein.dms.api.controllers.directory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.Ahmed_Zein.dms.api.models.AddDirectoryBody;
import com.github.Ahmed_Zein.dms.models.Directory;
import com.github.Ahmed_Zein.dms.models.dao.LocalUserDAO;
import com.github.Ahmed_Zein.dms.services.JWTService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class DirectoryControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private LocalUserDAO localUserDAO;
    @Autowired
    private JWTService jwtService;

    private final ObjectMapper mapper;

    DirectoryControllerTest() {
        this.mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    private String generateToken(Long userId) throws Exception {
        var user = localUserDAO.findById(userId).orElseThrow(Exception::new);
        return jwtService.generateToken(user);
    }

    @Test
    public void getDirectories_ADMIN_Success() throws Exception {
        var token = generateToken(2L);
        ResultActions resultActions = mvc.perform(get("/users/1/workspace/directories").header("Authorization", "Bearer " + token)).andExpect(status().isOk());

        resultActions.andExpect(result -> {
            String json = result.getResponse().getContentAsString();
            List<Directory> directories = mapper.readValue(json, new TypeReference<List<Directory>>() {
            });
            System.out.println(directories.size());
            Assertions.assertEquals(directories.size(), 2);
            directories.forEach(directory -> Assertions.assertEquals(directory.getLocalUser().getId(), 1));
        });
    }

    @Test
    public void getDirectories_Success() throws Exception {
        var token = generateToken(1L);
        ResultActions resultActions = mvc.perform(get("/users/1/workspace/directories").header("Authorization", "Bearer " + token)).andExpect(status().isOk());

        resultActions.andExpect(result -> {
            String json = result.getResponse().getContentAsString();
            List<Directory> directories = mapper.readValue(json, new TypeReference<List<Directory>>() {
            });
            System.out.println(directories.size());
            Assertions.assertEquals(directories.size(), 2);
            directories.forEach(directory -> Assertions.assertEquals(directory.getLocalUser().getId(), 1));
        });
    }

    @Test
    public void getDirectories_Forbidden() throws Exception {
        var token = generateToken(1L);
        ResultActions result = mvc.perform(get("/users/99/workspace/directories").header("Authorization", "Bearer " + token)).andExpect(status().isForbidden());
    }

    @Test
    public void getDirectories_NotFound() throws Exception {
        var token = generateToken(2L);
        ResultActions result = mvc.perform(get("/users/99/workspace/directories").header("Authorization", "Bearer " + token)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void addDirectoryTest_SUCESS() throws Exception {
        var token = generateToken(1L);
        var body = AddDirectoryBody.builder()
                .name("FOLDER_1")
                .isPublic(false)
                .build();

        ResultActions resultActions = mvc.perform(post("/users/1/workspace/directories")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk());

        resultActions.andExpect(result -> {
            String json = result.getResponse().getContentAsString();
            var newDirectory = mapper.readValue(json, Directory.class);
            Assertions.assertEquals(newDirectory.getName(), body.getName());
            Assertions.assertEquals(newDirectory.getIsPublic(), body.getIsPublic());
            Assertions.assertEquals(newDirectory.getLocalUser().getId(), 1L);
        });
    }

    @Test
    @Transactional
    public void addDirectoryTest_ADMIN_SUCESS() throws Exception {
        var token = generateToken(2L);
        var body = AddDirectoryBody.builder()
                .name("FOLDER_1")
                .isPublic(false)
                .build();

        ResultActions resultActions = mvc.perform(post("/users/1/workspace/directories")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk());

        resultActions.andExpect(result -> {
            String json = result.getResponse().getContentAsString();
            var newDirectory = mapper.readValue(json, Directory.class);
            Assertions.assertEquals(newDirectory.getName(), body.getName());
            Assertions.assertEquals(newDirectory.getIsPublic(), body.getIsPublic());
            Assertions.assertEquals(newDirectory.getLocalUser().getId(), 1L);
        });
    }

    @Test
    @Transactional
    public void addDirectoryTest_USER_NOTFOUND() throws Exception {
        var token = generateToken(2L);
        var body = AddDirectoryBody.builder()
                .name("FOLDER_1")
                .isPublic(false)
                .build();

        ResultActions resultActions = mvc.perform(post("/users/3/workspace/directories")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isNotFound());
    }


    @Test
    @Transactional
    public void getDirectoryTest_SUCESS() throws Exception {
        var token = generateToken(1L);

        ResultActions resultActions = mvc.perform(get("/users/1/workspace/directories/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        resultActions.andExpect(result -> {
            String json = result.getResponse().getContentAsString();
            var newDirectory = mapper.readValue(json, Directory.class);
            Assertions.assertEquals(newDirectory.getId(), 1L);
            Assertions.assertEquals(newDirectory.getLocalUser().getId(), 1L);
        });
    }

    @Test
    @Transactional
    public void getDirectoryTest_NOTFOUND() throws Exception {
        var token = generateToken(1L);

        mvc.perform(get("/users/1/workspace/directories/99")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void deleteDirectoryTest_SUCESS() throws Exception {
        var token = generateToken(1L);

        ResultActions resultActions = mvc.perform(delete("/users/1/workspace/directories/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void deleteDirectoryTest_ADMIN_SUCESS() throws Exception {
        var token = generateToken(2L);

        ResultActions resultActions = mvc.perform(delete("/users/1/workspace/directories/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void deleteDirectoryTest_ADMIN_NOTFOUND() throws Exception {
        var token = generateToken(2L);

        ResultActions resultActions = mvc.perform(delete("/users/1/workspace/directories/99")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }


    @Test
    @Transactional
    public void updateDirectoryTest_SUCESS() throws Exception {
        var token = generateToken(2L);
        var body = AddDirectoryBody.builder()
                .name("FOLDER_99")
                .build();
        System.out.println(body);
        ResultActions resultActions = mvc.perform(patch("/users/1/workspace/directories/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk());

        resultActions.andExpect(result -> {
            String json = result.getResponse().getContentAsString();
            var newDirectory = mapper.readValue(json, Directory.class);
            Assertions.assertEquals(newDirectory.getName(), body.getName());
            Assertions.assertEquals(newDirectory.getLocalUser().getId(), 1L);
        });
    }

    @Test
    @Transactional
    public void updateDirectoryTest_NOTFOUND() throws Exception {
        var token = generateToken(2L);
        var body = AddDirectoryBody.builder()
                .name("FOLDER_1")
                .isPublic(false)
                .build();

        ResultActions resultActions = mvc.perform(patch("/users/3/workspace/directories/99")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isNotFound());
    }


}
