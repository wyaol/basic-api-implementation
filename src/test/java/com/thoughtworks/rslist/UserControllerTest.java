package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    private ResultActions valid(UserDto userDto, String url, ResultMatcher resultMatcher) throws Exception {
        return mockMvc.perform(post(url)
                .content(new ObjectMapper().writeValueAsString(userDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(resultMatcher);
    }

    private ResultActions valid(UserDto userDto, String url, ResultMatcher resultMatcher, Integer index) throws Exception {
        return valid(userDto, url, resultMatcher).andExpect(header().string("index", String.valueOf(index)));
    }

    @Test
    void should_register_user() throws Exception {
        UserDto userDto = new UserDto("name", "gender", 18, "289672494@qq.com", "17307404504");
        valid(userDto, "/user/register", status().is(201), 0);
    }

    @Test
    void should_register_fail_when_name_is_empty() throws Exception {
        UserDto userDto = new UserDto("", "gender", 18, "289672494@qq.com", "17307404504");
        valid(userDto, "/user/register", status().isBadRequest());
    }

    @Test
    void should_register_fail_when_name_size_more_then_8() throws Exception {
        UserDto userDto = new UserDto("123456789", "gender", 18, "289672494@qq.com", "17307404504");
        valid(userDto, "/user/register", status().isBadRequest());
    }

    @Test
    void should_register_fail_when_gender_is_empty() throws Exception {
        UserDto userDto = new UserDto("name", "", 18, "289672494@qq.com", "17307404504");
        valid(userDto, "/user/register", status().isBadRequest());
    }

    @Test
    void should_register_fail_when_age_is_empty() throws Exception {
        UserDto userDto = new UserDto("name", "gender", null, "289672494@qq.com", "17307404504");
        valid(userDto, "/user/register", status().isBadRequest());
    }

    @Test
    void should_register_fail_when_age_is_less_then_18() throws Exception {
        UserDto userDto = new UserDto("name", "gender", 17, "289672494@qq.com", "17307404504");
        valid(userDto, "/user/register", status().isBadRequest());
    }

    @Test
    void should_register_fail_when_age_is_more_then_100() throws Exception {
        UserDto userDto = new UserDto("name", "gender", 101, "289672494@qq.com", "17307404504");
        valid(userDto, "/user/register", status().isBadRequest());
    }

    @Test
    void should_register_fail_when_email_not_standard() throws Exception {
        UserDto userDto = new UserDto("name", "gender", 18, "email", "17307404504");
        valid(userDto, "/user/register", status().isBadRequest());
    }

    @Test
    void should_register_fail_when_phone_not_standard() throws Exception {
        UserDto userDto = new UserDto("name", "gender", 18, "289672494@qq.com", "phone");
        valid(userDto, "/user/register", status().isBadRequest());
    }
}
