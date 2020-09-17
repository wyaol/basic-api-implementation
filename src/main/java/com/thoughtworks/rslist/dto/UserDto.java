package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NotEmpty
    @Size(max = 8)
    @JsonProperty(value = "user_name")
    private String name;

    @NotEmpty
    @JsonProperty(value = "user_gender")
    private String gender;

    @NotNull
    @Min(18)
    @Max(100)
    @JsonProperty(value = "user_age")
    private Integer age;

    @Pattern(regexp = "^.*?@.*?\\..*$")
    @JsonProperty(value = "user_email")
    private String email;

    @NotEmpty
    @Pattern(regexp = "^1\\d{10}")
    @JsonProperty(value = "user_phone")
    private String phone;
    private Integer vote = 10;

    public UserDto(String name, String gender, Integer age, String email, String phone) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.email = email;
        this.phone = phone;
        this.vote = 10;
    }
}
