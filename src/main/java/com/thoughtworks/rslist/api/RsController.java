package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.Event;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.exceptions.CommonException;
import com.thoughtworks.rslist.exceptions.InvalidParamException;
import com.thoughtworks.rslist.service.EventService;
import com.thoughtworks.rslist.service.UserService;
import com.thoughtworks.rslist.service.VoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
public class RsController {

    @Resource
    UserService userService;

    @Resource
    EventService eventService;

    @Resource
    VoteService voteService;

    @GetMapping("/rs/event/{id}")
    public ResponseEntity getOneEvent(@PathVariable int id) throws CommonException {
        Event event = eventService.getEventById(id);
        return ResponseEntity.status(HttpStatus.OK).body(event);
    }

//    @GetMapping("/rs/event")
//    public ResponseEntity getRangeEvents(@RequestParam(required = false, defaultValue = "1") Integer start,
//                                         @RequestParam(required = false, defaultValue = "-1") Integer end) throws InvalidRequestParamException {
//        if (start < 0 || end > rsList.size()) throw new InvalidRequestParamException("invalid request param");
//        return ResponseEntity.created(null).body(rsList.subList(start - 1, end));
//    }
//
    @GetMapping("/rs/event/list_all")
    public ResponseEntity getAllEvent() {
        List<Event> events = eventService.getEvents();
        return ResponseEntity.status(HttpStatus.OK).body(events);
    }

    @PostMapping("/rs/event")
    public ResponseEntity addOneEvent(@Valid @RequestBody Event event, BindingResult re) throws CommonException {
        if (re.getAllErrors().size() != 0) throw new InvalidParamException("invalid param");
        userService.getOneUser(event.getUserId());
        eventService.addOneEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/rs/{rsEventId}")
    public ResponseEntity editOneEvent(@PathVariable("rsEventId") int rsEventId, @RequestBody Event event) throws CommonException {
        eventService.updateEvent(rsEventId, event);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
//
//    @DeleteMapping("/rs/event/{index}")
//    public ResponseEntity deleteOneEvent(@PathVariable("index") int index) {
//        if (index < rsList.size()) rsList.remove(index - 1);
//        return ResponseEntity.created(null).header("index", String.valueOf(index - 1)).build();
//    }

    @PostMapping("/rs/vote/{rsEventId}")
    public ResponseEntity vote(@PathVariable int rsEventId, @RequestBody VoteDto voteDto) throws CommonException {
        voteDto.setEventId(rsEventId);
        voteService.vote(voteDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
