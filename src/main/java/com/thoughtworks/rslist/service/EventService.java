package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.Event;
import com.thoughtworks.rslist.entity.EventEntity;
import com.thoughtworks.rslist.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    @Resource
    EventRepository eventRepository;

    private Event eventEntityToEvent(EventEntity eventEntity) {
        return new Event(eventEntity.getEventName(), eventEntity.getKeyWord(), eventEntity.getUserId());
    }

    private EventEntity eventToEventEntity(Event event) {
        return EventEntity.builder()
                .eventName(event.getEventName())
                .keyWord(event.getKeyWord())
                .userId(event.getUserId())
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
}
