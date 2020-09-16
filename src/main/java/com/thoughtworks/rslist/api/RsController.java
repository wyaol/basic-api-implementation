package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.Event;
import com.thoughtworks.rslist.exceptions.InvalidIndexException;
import com.thoughtworks.rslist.exceptions.InvalidRequestParamException;
import org.hibernate.mapping.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.thoughtworks.rslist.utils.Utils;

@RestController
public class RsController {

    private List<Event> rsList = new ArrayList<Event>(Arrays.asList(
            new Event("第一条事件", "其他"),
            new Event("第二条事件", "其他"),
            new Event("第三条事件", "其他")
    ));

    @GetMapping("/rs/event/{index}")
    public ResponseEntity getOneEvent(@PathVariable int index) throws InvalidIndexException {
        if (index < 0 || index > rsList.size()) throw new InvalidIndexException("invalid index");
        return ResponseEntity.status(Utils.SUCCESS_CODE).header("index", String.valueOf(index - 1)).body(rsList.get(index - 1));
    }

    @GetMapping("rs/event")
    public ResponseEntity getRangeEvents(@RequestParam(required = false, defaultValue = "1") Integer start,
                                         @RequestParam(required = false, defaultValue = "-1") Integer end) throws InvalidRequestParamException {
        if (start < 0 || end > rsList.size()) throw new InvalidRequestParamException("invalid request param");
        return ResponseEntity.status(Utils.SUCCESS_CODE).body(rsList.subList(start - 1, end));
    }

    @GetMapping("rs/event/list_all")
    public ResponseEntity getAllEvent() {
        return ResponseEntity.status(Utils.SUCCESS_CODE).body(rsList);
    }

    @PostMapping("rs/event")
    public ResponseEntity addOneEvent(@RequestBody Event event) {
        rsList.add(event);
        return ResponseEntity.status(Utils.SUCCESS_CODE).header("index", String.valueOf(rsList.indexOf(event))).build();
    }

    @PutMapping("rs/event/{index}")
    public ResponseEntity editOneEvent(@PathVariable("index") int index, @RequestBody Event event) {
        if (event.getEventName() != null) rsList.get(index - 1).setEventName(event.getEventName());
        if (event.getKeyWord() != null) rsList.get(index - 1).setKeyWord(event.getKeyWord());
        return ResponseEntity.status(Utils.SUCCESS_CODE).header("index", String.valueOf(index - 1)).build();
    }

    @DeleteMapping("rs/event/{index}")
    public ResponseEntity deleteOneEvent(@PathVariable("index") int index) {
        if (index < rsList.size()) rsList.remove(index - 1);
        return ResponseEntity.status(Utils.SUCCESS_CODE).header("index", String.valueOf(index - 1)).build();
    }
}
