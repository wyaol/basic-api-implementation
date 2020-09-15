package com.thoughtworks.rslist.api;

import org.hibernate.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {
    private List<String> rsList = new ArrayList<String>(Arrays.asList("第一条事件", "第二条事件", "第三条事件"));

    @GetMapping("/rs/event/{index}")
    public String getOneEvent(@PathVariable int index) {
        return rsList.get(index - 1);
    }

    @GetMapping("rs/event")
    public String getRangeEvents(@RequestParam(required = false, defaultValue = "1") Integer start,
                          @RequestParam(required = false, defaultValue = "2") Integer end) {
      return rsList.subList(start-1, end).toString();
    }

    @GetMapping("rs/event/list_all")
    public String getAllEvent() {
        return rsList.toString();
    }

    @PostMapping("rs/event")
    public void addOneEvent(@RequestBody String event) {
        rsList.add(event);
    }
}
