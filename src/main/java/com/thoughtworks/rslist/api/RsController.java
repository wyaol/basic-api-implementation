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

    @GetMapping("/events/{id}")
    public ResponseEntity getOneEvent(@PathVariable int id) throws CommonException {
        Event event = eventService.getEventById(id);
        return ResponseEntity.status(HttpStatus.OK).header("Accept", "application/json").body(event);
    }

    @GetMapping("/events")
    public ResponseEntity getRangeEvents(@RequestParam(required = false) Integer start,
                                         @RequestParam(required = false) Integer end) throws InvalidRequestParamException {
        List<Event> events = eventService.getEvents();
        if (start == null || end == null) events = eventService.getEvents();
        else if (start < 0 || end > events.size()) throw new InvalidRequestParamException("invalid request param");
        else {
            events = events.subList(start - 1, end);
        }
        return ResponseEntity.status(HttpStatus.OK).header("Accept", "application/json").body(events);
    }

    @PostMapping("/events")
    public ResponseEntity addOneEvent(@Valid @RequestBody Event event, BindingResult re) throws CommonException {
        if (re.getAllErrors().size() != 0) throw new InvalidParamException("invalid param");
        userService.getOneUser(event.getUserId());
        eventService.addOneEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).header("Accept", "application/json").build();
    }

    @PatchMapping("/events/{rsEventId}")
    public ResponseEntity editOneEvent(@PathVariable("rsEventId") int rsEventId, @RequestBody Event event) throws CommonException {
        eventService.updateEvent(rsEventId, event);
        return ResponseEntity.status(HttpStatus.OK).header("Accept", "application/json").build();
    }

    @DeleteMapping("/events/{id}")
    public ResponseEntity deleteOneEvent(@PathVariable("id") int id) {
        eventService.deleteEventById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).header("Accept", "application/json").build();
    }
}
