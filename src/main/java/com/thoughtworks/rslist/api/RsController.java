package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.Event;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.exceptions.CommonException;
import com.thoughtworks.rslist.exceptions.InvalidIndexException;
import com.thoughtworks.rslist.exceptions.InvalidParamException;
import com.thoughtworks.rslist.exceptions.InvalidRequestParamException;
import com.thoughtworks.rslist.service.EventService;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {

    @Autowired
    UserService userService;

    @Autowired
    EventService eventService;

//    @GetMapping("/rs/event/{index}")
//    public ResponseEntity getOneEvent(@PathVariable int index) throws InvalidIndexException {
//        if (index < 0 || index > rsList.size()) throw new InvalidIndexException("invalid index");
//        return ResponseEntity.created(null).header("index", String.valueOf(index - 1)).body(rsList.get(index - 1));
//    }
//
//    @GetMapping("/rs/event")
//    public ResponseEntity getRangeEvents(@RequestParam(required = false, defaultValue = "1") Integer start,
//                                         @RequestParam(required = false, defaultValue = "-1") Integer end) throws InvalidRequestParamException {
//        if (start < 0 || end > rsList.size()) throw new InvalidRequestParamException("invalid request param");
//        return ResponseEntity.created(null).body(rsList.subList(start - 1, end));
//    }
//
//    @GetMapping("/rs/event/list_all")
//    public ResponseEntity getAllEvent() {
//        return ResponseEntity.created(null).body(rsList);
//    }

    @PostMapping("/rs/event")
    public ResponseEntity addOneEvent(@Valid @RequestBody Event event, BindingResult re) throws CommonException {
        if (re.getAllErrors().size() != 0) throw new InvalidParamException("invalid param");
        userService.getOneUser(event.getUserId());
        Integer eventId = eventService.addOneEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).header("index", String.valueOf(eventId)).build();
    }

//    @PutMapping("/rs/event/{index}")
//    public ResponseEntity editOneEvent(@PathVariable("index") int index, @RequestBody Event event) {
//        if (event.getEventName() != null) rsList.get(index - 1).setEventName(event.getEventName());
//        if (event.getKeyWord() != null) rsList.get(index - 1).setKeyWord(event.getKeyWord());
//        return ResponseEntity.created(null).header("index", String.valueOf(index - 1)).build();
//    }
//
//    @DeleteMapping("/rs/event/{index}")
//    public ResponseEntity deleteOneEvent(@PathVariable("index") int index) {
//        if (index < rsList.size()) rsList.remove(index - 1);
//        return ResponseEntity.created(null).header("index", String.valueOf(index - 1)).build();
//    }

    private Boolean hasUserDto(UserDto userDto) {
        List<UserDto> userList = UserController.userDtoList;
        for (int i = 0; i < userList.size(); ++i) {
            if (userList.get(i).getName().equals(userDto.getName())) return true;
        }
        return false;
    }
}
