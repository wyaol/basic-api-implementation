package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class Event {
    private String eventName;
    private String keyWord;

//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Valid
    private UserDto userDto;

    public Event(String eventName, String keyWord) {
        this.eventName = eventName;
        this.keyWord = keyWord;
    }
}
