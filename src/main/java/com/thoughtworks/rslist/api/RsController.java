package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.Event;
import com.thoughtworks.rslist.exceptions.CommonException;
import com.thoughtworks.rslist.exceptions.InvalidParamException;
import com.thoughtworks.rslist.exceptions.InvalidRequestParamException;
import com.thoughtworks.rslist.service.EventService;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
public class RsController {

    private UserService userService;

    private EventService eventService;

    RsController(UserService userService, EventService eventService) {
        this.userService = userService;
        this.eventService = eventService;
    }

    @GetMapping("/rs/event/{id}")
    public ResponseEntity getOneEvent(@PathVariable int id) throws CommonException {
        Event event = eventService.getEventById(id);
        return ResponseEntity.status(HttpStatus.OK).body(event);
    }

    @GetMapping("/rs/event")
    public ResponseEntity getRangeEvents(@RequestParam(required = false, defaultValue = "1") Integer start,
                                         @RequestParam(required = false, defaultValue = "-1") Integer end) throws InvalidRequestParamException {
        List<Event> events = eventService.getEvents();
        if (start < 0 || end > events.size()) throw new InvalidRequestParamException("invalid request param");
        return ResponseEntity.created(null).body(events.subList(start - 1, end));
    }

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

    @DeleteMapping("/rs/event/{id}")
    public ResponseEntity deleteOneEvent(@PathVariable("id") int id) {
        List<Event> events = eventService.getEvents();
        eventService.deleteEventById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
