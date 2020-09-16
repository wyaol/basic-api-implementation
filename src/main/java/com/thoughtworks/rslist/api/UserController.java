package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.UserDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private List<UserDto> userDtoList = new ArrayList<>();

    @PostMapping("/user/register")
    public void Register(@RequestBody UserDto userDto) {
        userDtoList.add(userDto);
    }
}
