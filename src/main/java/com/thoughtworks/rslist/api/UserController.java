package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.exceptions.CommonException;
import com.thoughtworks.rslist.exceptions.InvalidUserException;
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
public class UserController {

    public static List<UserDto> userDtoList = new ArrayList<>(Arrays.asList(new UserDto("name", "gender", 18, "289672494@qq.com", "17307404504")));

    @Autowired
    UserService userService;

    @PostMapping("/user/register")
    public ResponseEntity register(@Valid @RequestBody UserDto userDto, BindingResult re) throws InvalidUserException {
        if (re.getAllErrors().size() != 0) throw new InvalidUserException("invalid user");
        Integer userId = userService.addOneUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).header("index", String.valueOf(userId)).build();
    }

    @GetMapping("/users")
    public ResponseEntity getUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUser());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity getOneUserById(@PathVariable(name = "userId") Integer userId) throws CommonException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getOneUser(userId));
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity deleteOneUserById(@PathVariable(name = "userId") Integer userId) throws CommonException {
        userService.deleteOneUser(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
