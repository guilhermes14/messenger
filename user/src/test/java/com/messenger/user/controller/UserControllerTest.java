package com.messenger.user.controller;

import com.messenger.user.dto.UserRecordDTO;
import com.messenger.user.models.UserModel;
import com.messenger.user.service.UserService;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;
    @Test
    void createUserRetorna201() throws Exception {
        UserRecordDTO dto = new UserRecordDTO("Guilherme", "teste@teste.com");

        UserModel userSalvo = new UserModel();
        userSalvo.setName("Guilherme");
        userSalvo.setEmail("teste@teste.com");

        when(userService.create(any(UserModel.class))).thenReturn(userSalvo);

        // Verifica requisicao post
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Guilherme"))
                .andExpect(jsonPath("$.email").value("teste@teste.com"));
    }
}