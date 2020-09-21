package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.Event;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.VoteDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RsListApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void should_fail_add_one_event_when_user_not_exist() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Event event = new Event("热搜事件名", "关键字", 1);
        String json = objectMapper.writeValueAsString(event);

        mockMvc.perform(post("/events").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_add_one_event() throws Exception {
        UserDto userDto = new UserDto("wan", "gender", 18, "289672494@qq.com", "17307404504");
        Event event = new Event("热搜事件名", "关键字", 1);
        addOneEventAndUser(userDto, event);
    }

    private void addOneEventAndUser(UserDto userDto, Event event) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/users/register").content(objectMapper.writeValueAsString(userDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        String json = objectMapper.writeValueAsString(event);

        mockMvc.perform(post("/events").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void should_edit_one_event() throws Exception {
        UserDto userDto = new UserDto("wan", "gender", 18, "289672494@qq.com", "17307404504");
        Event event = new Event("热搜事件名", "关键字", 1);
        addOneEventAndUser(userDto, event);

        ObjectMapper objectMapper = new ObjectMapper();
        Event newEvent = new Event("新的热搜事件名", "新的关键字", 1);
        String json = objectMapper.writeValueAsString(newEvent);

        mockMvc.perform(patch("/events/1").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/events/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("新的热搜事件名")));
    }

    @Test
    void should_delete_one_event() throws Exception {
        UserDto userDto = new UserDto("wan", "gender", 18, "289672494@qq.com", "17307404504");
        Event event = new Event("热搜事件名", "关键字", 1);
        addOneEventAndUser(userDto, event);

        mockMvc.perform(delete("/events/1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

    }

    @Test
    void should_throw_when_invalid_request_param() throws Exception {
        mockMvc.perform(get("/events?start=1&end=4"))
                .andExpect(jsonPath("$.error", is("invalid request param")));
    }

    @Test
    void should_vote() throws Exception {
        UserDto userDto = new UserDto("wan", "gender", 18, "289672494@qq.com", "17307404504");
        Event event = new Event("热搜事件名", "关键字", 1);
        addOneEventAndUser(userDto, event);

        VoteDto voteDto = new VoteDto(1, 1, 5, LocalDateTime.now());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(voteDto);
        mockMvc.perform(post("/votes/1").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/events/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.voteNum", is(5)));
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vote", is(5)));
    }
}
