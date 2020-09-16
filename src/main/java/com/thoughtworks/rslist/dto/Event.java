package com.thoughtworks.rslist.dto;

import lombok.*;

@Getter
@Setter
public class Event {
    private String eventName;
    private String keyWord;
    private UserDto userDto;

    public Event(String eventName, String keyWord) {
        this.eventName = eventName;
        this.keyWord = keyWord;
    }
}
