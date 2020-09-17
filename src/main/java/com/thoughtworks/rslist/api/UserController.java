package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.exceptions.InvalidUserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class UserController {
    public static List<UserDto> userDtoList = new ArrayList<>(Arrays.asList(new UserDto("name", "gender", 18, "289672494@qq.com", "17307404504")));

    @PostMapping("/user/register")
    public ResponseEntity register(@Valid @RequestBody UserDto userDto, BindingResult re) throws InvalidUserException {
        if (re.getAllErrors().size() != 0) throw new InvalidUserException("invalid user");
        userDtoList.add(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).header("index", String.valueOf(userDtoList.indexOf(userDto))).build();
    }

    @GetMapping("/users")
    public ResponseEntity getUsers() {
        return ResponseEntity.created(null).body(userDtoList);
    }
}
