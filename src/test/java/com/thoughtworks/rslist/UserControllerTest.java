package com.thoughtworks.rslist;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void should_register_user() throws Exception {
        mockMvc.perform(post("/user/register")
                .content(new ObjectMapper().writeValueAsString(new UserDto("name", "gender", 18, "email", "phone")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void should_not_register_success_when_name_is_empty() throws Exception {
        mockMvc.perform(post("/user/register")
                .content(new ObjectMapper().writeValueAsString(new UserDto("", "gender", 18, "email", "phone")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_register_success_when_name_size_more_then_8() throws Exception {
        mockMvc.perform(post("/user/register")
                .content(new ObjectMapper().writeValueAsString(new UserDto("123456789", "gender", 18, "email", "phone")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_register_success_when_gender_is_empty() throws Exception {
        mockMvc.perform(post("/user/register")
                .content(new ObjectMapper().writeValueAsString(new UserDto("name", "", 18, "email", "phone")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
