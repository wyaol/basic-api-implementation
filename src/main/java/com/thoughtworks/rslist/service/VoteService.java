package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.Event;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.exceptions.CommonException;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VoteService {

    private VoteRepository voteRepository;

    private UserService userService;

    private EventService eventService;

    VoteService(VoteRepository voteRepository, UserService userService, EventService eventService) {
        this.voteRepository = voteRepository;
        this.userService = userService;
        this.eventService = eventService;
    }

    private VoteEntity voteDtoToVoteEntity(VoteDto voteDto) {
        return VoteEntity.builder()
                .eventId(voteDto.getEventId())
                .userId(voteDto.getUserId())
                .voteNum(voteDto.getVoteNum())
                .voteTime(voteDto.getVoteTime())
                .build();
    }

    private VoteDto voteEntityToVoteDto(VoteEntity voteEntity) {
        return new VoteDto(voteEntity.getUserId(), voteEntity.getEventId(), voteEntity.getVoteNum(), voteEntity.getVoteTime());
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

    public List<VoteDto> getVotesByUserIdAndEventId(Integer userId, Integer eventId, Pageable pageable) {
        List<VoteEntity> voteEntities = voteRepository.findAllByUserIdAndEventId(userId, eventId, pageable);
        List<VoteDto> voteDtos = new ArrayList<>();
        voteEntities.forEach(voteEntity -> voteDtos.add(voteEntityToVoteDto(voteEntity)));
        return voteDtos;
    }

    public List<VoteDto> getVotesByStartAndEnd(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        List<VoteEntity> voteEntities = voteRepository.findAllByVoteTimeBetween(start, end, pageable);
        List<VoteDto> voteDtos = new ArrayList<>();
        voteEntities.forEach(voteEntity -> voteDtos.add(voteEntityToVoteDto(voteEntity)));
        return voteDtos;
    }
}
