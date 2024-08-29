package com.github.Ahmed_Zein.dms.api.controllers.workspace;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.Ahmed_Zein.dms.api.models.WorkspaceUpdate;
import com.github.Ahmed_Zein.dms.models.dao.LocalUserDAO;
import com.github.Ahmed_Zein.dms.services.JWTService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WorkspaceControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private LocalUserDAO localUserDAO;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @Transactional
    public void updateWorkSpaceName_Success() throws Exception {
        var user = localUserDAO.findById(1L).orElseThrow(Exception::new);
        String token = jwtService.generateToken(user);
        String newName = "new_workspace";
        var workspaceupdate = WorkspaceUpdate.builder().name(newName).build();
        ResultActions result = mvc.perform(patch("/v1/users/1/workspace")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(workspaceupdate))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
        result.andExpect(jsonPath("$.workspaceName").exists())
                .andExpect(jsonPath("$.workspaceName").isNotEmpty())
                .andExpect(jsonPath("$.workspaceName").value(newName));

    }

    @Test
    @Transactional
    public void updateWorkSpaceName_Forbidden() throws Exception {
        var user = localUserDAO.findById(1L).orElseThrow(Exception::new);
        String token = jwtService.generateToken(user);
        String newName = "new_workspace";
        var workspaceupdate = WorkspaceUpdate.builder().name(newName).build();
        ResultActions result = mvc.perform(patch("/v1/users/99/workspace")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(workspaceupdate))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    public void updateWorkSpaceName_UserNotFound() throws Exception {
        var user = localUserDAO.findById(2L).orElseThrow(Exception::new);
        String token = jwtService.generateToken(user);
        String newName = "new_workspace";
        var workspaceupdate = WorkspaceUpdate.builder().name(newName).build();
        ResultActions result = mvc.perform(patch("/v1/users/99/workspace")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(workspaceupdate))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

}
