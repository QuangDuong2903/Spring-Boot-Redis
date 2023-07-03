package com.quangduong.redis.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quangduong.redis.dto.user.UserDTO;
import com.quangduong.redis.service.IUserService;
import com.quangduong.redis.utils.JwtUtils;
import com.quangduong.redis.utils.SecurityUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthAPI.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthAPITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private SecurityUtils securityUtils;

    @MockBean
    private IUserService userService;

    @MockBean
    private JwtUtils jwtUtils;

    @Test
    @DisplayName("Should sign up")
    public void shouldSignUp() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("quangduong");
        userDTO.setPassword("292003");

        Mockito.when(userService.createUser(Mockito.any(UserDTO.class))).thenReturn(userDTO);
        Mockito.when(jwtUtils.generateToken(Mockito.anyString())).thenReturn("Siuuuuuuu");

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("quangduong"))
                .andExpect(jsonPath("$.token").value("Siuuuuuuu"));
    }

}