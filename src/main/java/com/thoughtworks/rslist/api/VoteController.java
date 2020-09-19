package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.exceptions.CommonException;
import com.thoughtworks.rslist.service.VoteService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
public class VoteController {

    private VoteService voteService;

    VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping("/rs/vote/{rsEventId}")
    public ResponseEntity vote(@PathVariable int rsEventId, @RequestBody VoteDto voteDto) throws CommonException {
        voteDto.setEventId(rsEventId);
        voteService.vote(voteDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/vote")
    public ResponseEntity getVotesByStartAndEnd(
            @RequestParam(name = "start") String start,
            @RequestParam(name = "end") String end,
            @PageableDefault(value = 5, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) throws JsonProcessingException {
        DateTimeFormatter timeDtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse(start, timeDtf);
        LocalDateTime endTime = LocalDateTime.parse(end, timeDtf);
        List<VoteDto> voteDtos = voteService.getVotesByStartAndEnd(startTime, endTime, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(voteDtos);
    }
}
