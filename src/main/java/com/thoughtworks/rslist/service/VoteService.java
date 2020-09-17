package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.Event;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.*;
import com.thoughtworks.rslist.exceptions.CommonException;
import com.thoughtworks.rslist.repository.*;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

@Service
public class VoteService {

    @Resource
    VoteRepository voteRepository;

    @Resource
    UserService userService;

    @Resource
    EventService eventService;

    private VoteEntity voteDtoToVoteEntity(VoteDto voteDto) {
        return VoteEntity.builder()
                .eventId(voteDto.getEventId())
                .userId(voteDto.getUserId())
                .voteNum(voteDto.getVoteNum())
                .voteTime(voteDto.getVoteTime())
                .build();
    }

    @Transactional(rollbackOn = Exception.class)
    public void vote(VoteDto voteDto) throws CommonException {
        voteRepository.save(voteDtoToVoteEntity(voteDto));

        UserDto userDto = userService.getOneUser(voteDto.getUserId());
        int num = userDto.getVote() - voteDto.getVoteNum();
        if (num < 0) throw new CommonException("vote num is not enough, you have " + userDto.getVote());
        userDto.setVote(num);
        userService.updateUserById(voteDto.getUserId(), userDto);

        Event event = eventService.getEventById(voteDto.getEventId());
        event.setVoteNum(event.getVoteNum() + voteDto.getVoteNum());
        eventService.updateEvent(voteDto.getEventId(), event);
    }
}
