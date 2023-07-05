package com.quangduong.redis.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quangduong.redis.dto.user.UserDTO;
import com.quangduong.redis.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthAPITest extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should create user")
    @Order(1)
    public void shouldCreateUser() throws Exception {

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("quangduong");
        userDTO.setPassword("292003");

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("quangduong"))
                .andExpect(jsonPath("$.token").isString())
                .andDo(print());
    }


    @Test
    @DisplayName("Should not create user")
    @Order(2)
    public void shouldNotCreateUser() throws Exception {

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("quangduong");
        userDTO.setPassword("292003");

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value("409"))
                .andExpect(jsonPath("$.error").value("Username already exists"))
                .andDo(print());
    }

    @Test
    @DisplayName("Should signin")
    @Order(3)
    public void shouldSignIn() throws Exception {

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("quangduong");
        userDTO.setPassword("292003");

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("quangduong"))
                .andExpect(jsonPath("$.token").isString())
                .andDo(print());
    }

    @Test
    @DisplayName("Should not signin")
    @Order(3)
    public void shouldNotSignIn() throws Exception {

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("quangduong");
        userDTO.setPassword("15092003");

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.error").value("Bad credentials"))
                .andDo(print());
    }

}
