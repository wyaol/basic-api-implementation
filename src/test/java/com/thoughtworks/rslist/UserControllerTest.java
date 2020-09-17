package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.Event;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Test
    void should_delete_user_by_id() throws Exception {
        UserDto userDto = new UserDto("wan", "gender", 18, "289672494@qq.com", "17307404504");
        validPost(userDto, "/user/register", status().isCreated());
        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

    }

    @Test
    void should_delete_user_and_delete_events_by_id() throws Exception {
        // 添加用户
        ObjectMapper objectMapper = new ObjectMapper();
        UserDto userDto = new UserDto("wan", "gender", 18, "289672494@qq.com", "17307404504");
        mockMvc.perform(post("/user/register").content(objectMapper.writeValueAsString(userDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        // 添加时事件
        Event event = new Event("热搜事件名", "关键字", 1);
        String json = objectMapper.writeValueAsString(event);
        mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("index", "1"));

        // 删除用户
        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isOk());

        // 查看用户和事件
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
        mockMvc.perform(get("/rs/event/list_all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

    }
}
