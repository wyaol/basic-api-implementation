package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.Event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RsListApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void show_get_one_event() throws Exception {
        mockMvc.perform(get("/rs/event/1"))
                .andExpect(status().is(201))
                .andExpect(header().string("index", "0"))
                .andExpect(jsonPath("$.eventName", is("第一条事件")));
    }

    @Test
    void show_get_range_event() throws Exception {
        mockMvc.perform(get("/rs/event?start=1&end=3"))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")));
    }

    @Test
    void show_add_one_event() throws Exception {
        mockMvc.perform(get("/rs/event/list_all"))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")));

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/rs/event").content(objectMapper.writeValueAsString(new Event("第四条事件", "无分类"))).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201))
                .andExpect(header().string("index", "3"));

        mockMvc.perform(get("/rs/event/list_all"))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[3].eventName", is("第四条事件")));

    }

    @Test
    void should_edit_one_event() throws Exception {
        mockMvc.perform(get("/rs/event/1"))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.eventName", is("第一条事件")));
        mockMvc.perform(put("/rs/event/1").content(new ObjectMapper().writeValueAsString(new Event("第一件修改后的事件", null))).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201))
                .andExpect(header().string("index", "0"));
        mockMvc.perform(get("/rs/event/1"))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.eventName", is("第一件修改后的事件")));
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
}
