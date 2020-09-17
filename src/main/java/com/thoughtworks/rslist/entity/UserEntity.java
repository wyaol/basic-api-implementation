package com.thoughtworks.rslist.entity;

import com.thoughtworks.rslist.dto.UserDto;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String gender;
    private Integer age;
    private String email;
    private String phone;
    private Integer vote;

    @OneToMany(mappedBy = "userId", cascade = CascadeType.REMOVE)
    List<EventEntity> eventEntities;

    public void setByUserDto(UserDto userDto) {
        if (userDto.getVote() != null) this.vote = userDto.getVote();
        if (userDto.getName() != null) this.name = userDto.getName();
        if (userDto.getAge() != null) this.age = userDto.getAge();
        if (userDto.getEmail() != null) this.email = userDto.getEmail();
        if (userDto.getGender() != null) this.gender = userDto.getGender();
        if (userDto.getPhone() != null) this.phone = userDto.getPhone();
    }
}
