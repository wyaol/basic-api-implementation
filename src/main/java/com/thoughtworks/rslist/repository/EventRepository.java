package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.EventEntity;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<EventEntity, Integer> {
}
