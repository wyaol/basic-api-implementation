package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.Event;
import com.thoughtworks.rslist.entity.EventEntity;
import com.thoughtworks.rslist.exceptions.CommonException;
import com.thoughtworks.rslist.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Resource
    EventRepository eventRepository;

    private Event eventEntityToEvent(EventEntity eventEntity) {
        return new Event(eventEntity.getEventName(), eventEntity.getKeyWord(), eventEntity.getUserId(), eventEntity.getVoteNum());
    }

    private EventEntity eventToEventEntity(Event event) {
        return EventEntity.builder()
                .eventName(event.getEventName())
                .keyWord(event.getKeyWord())
                .userId(event.getUserId())
                .voteNum(event.getVoteNum())
                .build();
    }

    public Integer addOneEvent(Event event) {
        return eventRepository.save(eventToEventEntity(event)).getId();
    }

    public List<Event> getEvents() {
        List<Event> events = new ArrayList<>();
        Iterable<EventEntity> eventEntities = eventRepository.findAll();
        eventEntities.forEach(eventEntity -> events.add(eventEntityToEvent(eventEntity)));
        return events;
    }

    public void updateEvent(Integer eventId, Event event) throws CommonException {
        Optional<EventEntity> res = eventRepository.findById(eventId);
        if (!res.isPresent()) throw new CommonException(String.format("can not find event by id %d", eventId));
        EventEntity eventEntity = res.get();
        if (!eventEntity.getUserId().equals(event.getUserId())) throw new CommonException("user id mismatch event");
        eventEntity.setByEvent(event);
        eventRepository.save(eventEntity);
    }

    public Event getEventById(Integer id) throws CommonException {
        Optional<EventEntity> res = eventRepository.findById(id);
        if (res.isPresent()) return eventEntityToEvent(res.get());
        else throw new CommonException(String.format("can not find event by id %d", id));
    }

    public void deleteEventById(Integer id) {
        eventRepository.deleteById(id);
    }
}
