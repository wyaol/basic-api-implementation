package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.Event;
import com.thoughtworks.rslist.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RsListApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void should_get_one_event() throws Exception {
        mockMvc.perform(get("/rs/event/1"))
                .andExpect(status().is(201))
                .andExpect(header().string("index", "0"))
                .andExpect(jsonPath("$.eventName", is("第一条事件")))
//                .andExpect(jsonPath("$", not(hasKey("userDto"))));
        ;
    }

    @Test
    void should_get_range_event() throws Exception {
        mockMvc.perform(get("/rs/event?start=1&end=3"))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")));
    }

    @Test
    void should_fail_add_one_event_when_user_not_exist() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Event event = new Event("热搜事件名", "关键字", 1);
        String json = objectMapper.writeValueAsString(event);

        mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_add_one_event() throws Exception {
        UserDto userDto = new UserDto("wan", "gender", 18, "289672494@qq.com", "17307404504");
        Event event = new Event("热搜事件名", "关键字", 1);
        addOneEvent(userDto, event);
    }

    private void addOneEvent(UserDto userDto, Event event) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/user/register").content(objectMapper.writeValueAsString(userDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        String json = objectMapper.writeValueAsString(event);

        mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void should_edit_one_event() throws Exception {
        UserDto userDto = new UserDto("wan", "gender", 18, "289672494@qq.com", "17307404504");
        Event event = new Event("热搜事件名", "关键字", 1);
        addOneEvent(userDto, event);

        ObjectMapper objectMapper = new ObjectMapper();
        Event newEvent = new Event("新的热搜事件名", "新的关键字", 1);
        String json = objectMapper.writeValueAsString(newEvent);

        mockMvc.perform(patch("/rs/1").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/event/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("新的热搜事件名")));
    }

    @Test
    void should_delete_one_event() throws Exception {
        mockMvc.perform(get("/rs/event/list_all"))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")));

        mockMvc.perform(delete("/rs/event/2"))
                .andExpect(status().is(201))
                .andExpect(header().string("index", "1"));;

        mockMvc.perform(get("/rs/event/list_all"))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[1].eventName", is("第三条事件")));

    }

    @Test
    void should_throw_when_invalid_request_param() throws Exception {
        mockMvc.perform(get("/rs/event?start=1&end=4"))
                .andExpect(jsonPath("$.error", is("invalid request param")));
    }

    @Test
    void should_throw_when_invalid_index() throws Exception {
        mockMvc.perform(get("/rs/event/5"))
                .andExpect(jsonPath("$.error", is("invalid index")));
    }

//    @Test
//    void should_throw_when_invalid_param() throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        Event event = new Event("添加一条热搜", "无分类");
//        UserDto userDto = new UserDto("xiaowang", "female", 17, "email", "18888888888");
//        event.setUserDto(userDto);
//        String json = objectMapper.writeValueAsString(event);
//        mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.error", is("invalid param")));
//    }
}
