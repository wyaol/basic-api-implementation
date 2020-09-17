package com.thoughtworks.rslist.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;
    private String gender;
    private Integer age;
    private String email;
    private String phone;
    private Integer vote;
}
