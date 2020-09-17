package com.thoughtworks.rslist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @NotEmpty
    private String eventName;

    @NotEmpty
    private String keyWord;

    @NotNull
    private Integer userId;

    private Integer voteNum;

    public Event(String eventName, String keyWord, Integer userId) {
        this.eventName = eventName;
        this.keyWord = keyWord;
        this.userId = userId;
        this.voteNum = 0;
    }
}
