package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.*;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    private ResultActions validPost(UserDto userDto, String url, ResultMatcher resultMatcher) throws Exception {
        return mockMvc.perform(post(url)
                .content(new ObjectMapper().writeValueAsString(userDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(resultMatcher);
    }

    private ResultActions validPost(UserDto userDto, String url, ResultMatcher resultMatcher, Integer index) throws Exception {
        return validPost(userDto, url, resultMatcher).andExpect(header().string("index", String.valueOf(index)));
    }

    @Test
    void should_register_user() throws Exception {
        UserDto userDto = new UserDto("wan", "gender", 18, "289672494@qq.com", "17307404504");
        validPost(userDto, "/user/register", status().is(201));
    }

    @Test
    void should_register_fail_when_name_is_empty() throws Exception {
        UserDto userDto = new UserDto("", "gender", 18, "289672494@qq.com", "17307404504");
        validPost(userDto, "/user/register", status().isBadRequest());
    }

    @Test
    void should_register_fail_when_name_size_more_then_8() throws Exception {
        UserDto userDto = new UserDto("123456789", "gender", 18, "289672494@qq.com", "17307404504");
        validPost(userDto, "/user/register", status().isBadRequest());
    }

    @Test
    void should_register_fail_when_gender_is_empty() throws Exception {
        UserDto userDto = new UserDto("name", "", 18, "289672494@qq.com", "17307404504");
        validPost(userDto, "/user/register", status().isBadRequest());
    }

    @Test
    void should_register_fail_when_age_is_empty() throws Exception {
        UserDto userDto = new UserDto("name", "gender", null, "289672494@qq.com", "17307404504");
        validPost(userDto, "/user/register", status().isBadRequest());
    }

    @Test
    void should_register_fail_when_age_is_less_then_18() throws Exception {
        UserDto userDto = new UserDto("name", "gender", 17, "289672494@qq.com", "17307404504");
        validPost(userDto, "/user/register", status().isBadRequest());
    }

    @Test
    void should_register_fail_when_age_is_more_then_100() throws Exception {
        UserDto userDto = new UserDto("name", "gender", 101, "289672494@qq.com", "17307404504");
        validPost(userDto, "/user/register", status().isBadRequest());
    }

    @Test
    void should_register_fail_when_email_not_standard() throws Exception {
        UserDto userDto = new UserDto("name", "gender", 18, "email", "17307404504");
        validPost(userDto, "/user/register", status().isBadRequest());
    }

    @Test
    void should_register_fail_when_phone_not_standard() throws Exception {
        UserDto userDto = new UserDto("name", "gender", 18, "289672494@qq.com", "phone");
        validPost(userDto, "/user/register", status().isBadRequest());
    }

    @Test
    void should_return_user_list() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].user_name", is("name")))
                .andExpect(jsonPath("$[0].user_gender", is("gender")))
                .andExpect(jsonPath("$[0].user_age", is(18)))
                .andExpect(jsonPath("$[0].user_email", is("289672494@qq.com")))
                .andExpect(jsonPath("$[0].user_phone", is("17307404504")));
    }

    @Test
    void should_throw_when_invalid_user() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        UserDto userDto = new UserDto("xiaowang", "female", 17, "email", "phone");
        String json = objectMapper.writeValueAsString(userDto);
        mockMvc.perform(post("/user/register").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    void should_get_user_by_id() throws Exception {
        UserDto userDto = new UserDto("wan", "gender", 18, "289672494@qq.com", "17307404504");
        validPost(userDto, "/user/register", status().isCreated());
        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_name", is("wan")));
    }
}
