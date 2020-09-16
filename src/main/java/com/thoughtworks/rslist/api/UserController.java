package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private List<UserDto> userDtoList = new ArrayList<>();

    @PostMapping("/user/register")
    public ResponseEntity register(@Valid @RequestBody UserDto userDto) {
        userDtoList.add(userDto);
        return ResponseEntity.status(Utils.SUCCESS_CODE).header("index", String.valueOf(userDtoList.indexOf(userDto))).build();
    }
}
