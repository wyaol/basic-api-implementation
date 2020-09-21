package com.thoughtworks.rslist;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VoteControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Resource
    VoteRepository voteRepository;

    @Test
    void should_return_votes_between_vote_time() throws Exception {
        String start = "2020-01-01 15:59:59";
        String end = "2020-01-04 16:59:59";
        String voteTime1 = "2020-01-01 11:11:11";
        String voteTime2 = "2020-01-02 11:11:11";
        String voteTime3 = "2020-01-03 11:11:11";

        DateTimeFormatter timeDtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime voteTime11 = LocalDateTime.parse(voteTime1, timeDtf);
        LocalDateTime voteTime22 = LocalDateTime.parse(voteTime2, timeDtf);
        LocalDateTime voteTime33 = LocalDateTime.parse(voteTime3, timeDtf);
        voteRepository.save(new VoteEntity(1, 1, 1, 1, voteTime11));
        voteRepository.save(new VoteEntity(2, 1, 1, 1, voteTime22));
        voteRepository.save(new VoteEntity(3, 1, 1, 1, voteTime33));

        mockMvc.perform(get(String.format("/votes?start=%s&end=%s&page=%d", start, end, 0)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void test() throws JsonProcessingException {
        VoteDto voteDto = new VoteDto(1, 1, 5, LocalDateTime.now());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(voteDto);
        System.out.println(json);
    }
}
