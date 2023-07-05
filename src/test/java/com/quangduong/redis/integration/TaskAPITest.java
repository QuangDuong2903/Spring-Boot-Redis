package com.quangduong.redis.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quangduong.redis.dto.todo.TaskDTO;
import com.quangduong.redis.entity.UserEntity;
import com.quangduong.redis.repository.UserRepository;
import com.quangduong.redis.utils.JwtUtils;
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
public class TaskAPITest extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    @DisplayName("Should throw auth exception")
    @Order(1)
    public void shouldThrowAuthException() throws Exception {

        TaskDTO dto = new TaskDTO();
        dto.setTitle("quang duong task");
        dto.setDone(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.status").value("401"))
                .andDo(print());

    }

    @Test
    @DisplayName("Should create task")
    @Order(2)
    public void shouldCreateTask() throws Exception {

        UserEntity user = new UserEntity();
        user.setUsername("quangduong");
        user.setPassword("292003");
        userRepository.save(user);
        String token = jwtUtils.generateToken("quangduong");

        TaskDTO dto = new TaskDTO();
        dto.setTitle("quang duong task");
        dto.setDone(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/tasks")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("quang duong task"))
                .andExpect(jsonPath("$.done").value(true))
                .andDo(print());

    }

    @Test
    @DisplayName("Should validate create task request")
    @Order(3)
    public void shouldValidateCreateTaskRequest() throws Exception {

        String token = jwtUtils.generateToken("quangduong");

        TaskDTO dto = new TaskDTO();
        dto.setTitle("   ");
        dto.setDone(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/tasks")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Title is required"))
                .andExpect(jsonPath("$.status").value("400"))
                .andDo(print());

        dto.setTitle("duong task");
        dto.setDone(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/tasks")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Status is required"))
                .andExpect(jsonPath("$.status").value("400"))
                .andDo(print());

    }

    @Test
    @DisplayName("Should update task")
    @Order(4)
    public void shouldUpdateTask() throws Exception {

        String token = jwtUtils.generateToken("quangduong");

        TaskDTO dto = new TaskDTO();
        dto.setDone(false);

        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("quang duong task"))
                .andExpect(jsonPath("$.done").value(false))
                .andDo(print());

        dto.setDone(null);
        dto.setTitle("Duong task");

        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Duong task"))
                .andExpect(jsonPath("$.done").value(false))
                .andDo(print());

    }

    @Test
    @DisplayName("Should not update task")
    @Order(5)
    public void shouldNotUpdateTask() throws Exception {

        UserEntity user = new UserEntity();
        user.setUsername("nhatgiang");
        user.setPassword("15092003");
        userRepository.save(user);
        String token = jwtUtils.generateToken("nhatgiang");

        TaskDTO dto = new TaskDTO();
        dto.setDone(false);

        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/2")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Not found todo with id: 2"))
                .andExpect(jsonPath("$.status").value("400"))
                .andDo(print());

        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Not allowed"))
                .andExpect(jsonPath("$.status").value("403"))
                .andDo(print());

    }

    @Test
    @DisplayName("Should not delete task")
    @Order(6)
    public void shouldNotDeleteTask() throws Exception {

        String token = jwtUtils.generateToken("nhatgiang");

        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/2")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Not found todo with id: 2"))
                .andExpect(jsonPath("$.status").value("400"))
                .andDo(print());

        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Not allowed"))
                .andExpect(jsonPath("$.status").value("403"))
                .andDo(print());
    }

    @Test
    @DisplayName("Should delete task")
    @Order(7)
    public void shouldDeleteTask() throws Exception {

        String token = jwtUtils.generateToken("quangduong");

        TaskDTO dto = new TaskDTO();
        dto.setDone(false);

        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

}
