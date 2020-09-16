package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.Event;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {

    private List<Event> rsList = new ArrayList<Event>(Arrays.asList(
            new Event("第一条事件", "其他"),
            new Event("第二条事件", "其他"),
            new Event("第三条事件", "其他")
    ));

    @GetMapping("/rs/event/{index}")
    public ResponseEntity getOneEvent(@PathVariable int index) {
        return ResponseEntity.status(201).body(rsList.get(index - 1));
    }

    @GetMapping("rs/event")
    public ResponseEntity getRangeEvents(@RequestParam(required = false, defaultValue = "1") Integer start,
                                      @RequestParam(required = false, defaultValue = "2") Integer end) {
        return ResponseEntity.status(201).body(rsList.subList(start - 1, end));
    }

    @GetMapping("rs/event/list_all")
    public ResponseEntity getAllEvent() {
        return ResponseEntity.status(201).body(rsList);
    }

    @PostMapping("rs/event")
    public ResponseEntity addOneEvent(@RequestBody Event event) {
        rsList.add(event);
        return ResponseEntity.status(201).build();
    }

    @PutMapping("rs/event/{index}")
    public ResponseEntity editOneEvent(@PathVariable("index") int index, @RequestBody Event event) {
        if (event.getEventName() != null) rsList.get(index - 1).setEventName(event.getEventName());
        if (event.getKeyWord() != null) rsList.get(index - 1).setKeyWord(event.getKeyWord());
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("rs/event/{index}")
    public ResponseEntity deleteOneEvent(@PathVariable("index") int index) {
        if (index < rsList.size()) rsList.remove(index - 1);
        return ResponseEntity.status(201).build();
    }
}
