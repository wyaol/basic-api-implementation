package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.exceptions.CommonException;
import com.thoughtworks.rslist.exceptions.InvalidUserException;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {

    private UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users/register")
    public ResponseEntity register(@Valid @RequestBody UserDto userDto, BindingResult re) throws InvalidUserException {
        if (re.getAllErrors().size() != 0) throw new InvalidUserException("invalid user");
        userService.addOneUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).header("Accept", "application/json").build();
    }

    @GetMapping("/users")
    public ResponseEntity getUsers() {
        return ResponseEntity.status(HttpStatus.OK).header("Accept", "application/json").body(userService.getAllUser());
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity getOneUserById(@PathVariable(name = "userId") Integer userId) throws CommonException {
        return ResponseEntity.status(HttpStatus.OK).header("Accept", "application/json").body(userService.getOneUser(userId));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity deleteOneUserById(@PathVariable(name = "userId") Integer userId) throws CommonException {
        userService.deleteOneUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).header("Accept", "application/json").build();
    }
}
