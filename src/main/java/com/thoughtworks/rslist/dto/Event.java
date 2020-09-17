package com.thoughtworks.rslist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class Event {
    @NotEmpty
    private String eventName;

    @NotEmpty
    private String keyWord;

    @NotNull
    private Integer userId;
}
