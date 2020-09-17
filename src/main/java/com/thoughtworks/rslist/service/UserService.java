package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exceptions.CommonException;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public Integer addOneUser(UserDto userDto) {
        return userRepository.save(userDtoToUserEntity(userDto)).getId();
    }

    public List<UserDto> getAllUser() {
        List<UserEntity> userEntities = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        userEntities.forEach(userEntity -> userDtos.add(userEntityToUserDto(userEntity)));
        return userDtos;
    }

    public UserDto getOneUser(Integer userId) throws CommonException {
        Optional<UserEntity> res =  userRepository.findById(userId);
        if (res.isPresent())
            return userEntityToUserDto(res.get());
        else
            throw new CommonException(String.format("can not find user by id %d", userId));
    }

    private UserEntity userDtoToUserEntity(UserDto userDto) {
        return UserEntity.builder()
                .name(userDto.getName())
                .gender(userDto.getGender())
                .age(userDto.getAge())
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .vote(userDto.getVote())
                .build();
    }

    private UserDto userEntityToUserDto(UserEntity userEntity) {
        return new UserDto(
                userEntity.getName(),
                userEntity.getGender(),
                userEntity.getVote(),
                userEntity.getEmail(),
                userEntity.getPhone(),
                userEntity.getVote()
        );
    }

}
