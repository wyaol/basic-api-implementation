package com.thoughtworks.rslist.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class Event {
    @NotEmpty
    private String eventName;

    @NotEmpty
    private String keyWord;

    @Valid
    @NotNull
    private UserDto userDto;

    public Event(String eventName, String keyWord) {
        this.eventName = eventName;
        this.keyWord = keyWord;
    }
}
